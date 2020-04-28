package com.ankitgh.mobiledatatrend.repository

import android.annotation.SuppressLint
import android.app.Application
import com.ankitgh.mobiledatatrend.database.Record
import com.ankitgh.mobiledatatrend.database.RecordDao
import com.ankitgh.mobiledatatrend.database.RecordsDatabase
import com.ankitgh.mobiledatatrend.noOfPages
import com.ankitgh.mobiledatatrend.pageSize
import com.ankitgh.mobiledatatrend.rest.RecordsDataApi
import com.ankitgh.mobiledatatrend.rest.RestConnector
import com.ankitgh.mobiledatatrend.rest.model.RecordYear
import com.ankitgh.mobiledatatrend.rest.model.RecordsBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 * Repository to handle data from server and DB and decide based
 * on availability as to which datasource to choose and propagate
 * data and errors to ViewModels accordingly
 */
@SuppressLint("LogConditional")
class RecordRepository(application: Application) {
    private var recordDao: RecordDao
    private var apiResponse: ApiResponse = ApiResponse()
    private val recordsDatabase: RecordsDatabase
    private val repoScope = CoroutineScope(Dispatchers.Main)

    init {
        val recordsDataApi: RecordsDataApi = RestConnector.instance
        recordsDatabase = RecordsDatabase.getInstance(application)
        recordDao = recordsDatabase.recordDao()

        //On Repo initialization request data from Server and update in Database
        repoScope.launch {
            requestRecordsFromRestService(recordsDataApi)
        }
    }

    fun getAllRecordsFromDB(): ApiResponse {
        apiResponse.data = recordDao.getAllRecords()
        return apiResponse
    }

    /**
     * Suspended function to insert data to DB.
     * which should be called from a coroutine scope
     */
    suspend fun insertDataFromServerToDB(recordList: List<Record>) {
        withContext(Dispatchers.IO) {
            recordDao.insertAllRecords(recordList)
            Timber.d("insertDataFromServerToDB -> Record DB populated ")
        }
    }

    /**
     * Fetches data from rest service and supplies upper models with errors and
     * refreshes Database
     */
    private suspend fun requestRecordsFromRestService(recordsDataApi: RecordsDataApi): ApiResponse {
        Timber.d("Starting to fetch records from WebService...")
        withContext(Dispatchers.IO) {
            val recordsApi: Call<RecordsBase> = recordsDataApi.getRecords(pageSize, noOfPages)

            //enqueue is called on a different thread so no need to run on different scope.
            recordsApi.enqueue(object : Callback<RecordsBase> {
                override fun onFailure(call: Call<RecordsBase>, error: Throwable) {
                    apiResponse.apiError.value = getErrorMessage(error)
                    Timber.e("Error while fetching data : ${error.message}")
                }

                override fun onResponse(call: Call<RecordsBase>, response: Response<RecordsBase>) {
                    val recordBase: RecordsBase? = response.body()
                    repoScope.launch {
                        if (response.isSuccessful) {
                            recordBase?.result?.records?.let { insertDataFromServerToDB(it) }
                        } else {
                            apiResponse.apiError.value =
                                "There is a problem refreshing data from API"
                        }
                    }
                    Timber.d("Api call success : ${recordBase?.result?.records.toString()}.")
                }
            })
        }
        return apiResponse
    }

    suspend fun sortRecordsBasedOnYear(recordsList: List<Record?>): ArrayList<RecordYear> {
        val recordHashMap: HashMap<Int, ArrayList<RecordYear>> = mapQuartersToYear(recordsList)
        val recordYearsFinalArrayList: ArrayList<RecordYear> = ArrayList()
        withContext(Dispatchers.Default) {
            for (recordEntry in recordHashMap.entries) {
                val arrayList: ArrayList<RecordYear> = recordEntry.value
                val totalSumOfUsage: Double = getTotalUsageInYear(arrayList)
                val dipInUsage: Boolean = wasDipInDataUsageInYear(arrayList)
                recordYearsFinalArrayList.add(
                    RecordYear(
                        recordEntry.key.toString(),
                        totalSumOfUsage,
                        dipInUsage
                    )
                )
            }
            recordYearsFinalArrayList.sortBy { it.quater }
        }
        val startIndex = recordYearsFinalArrayList.indexOfFirst { it.quater == "2008" }
        val endIndex = recordYearsFinalArrayList.indexOfFirst { it.quater == "2018" } + 1
        return ArrayList(recordYearsFinalArrayList.subList(startIndex, endIndex))
    }

    private fun mapQuartersToYear(recordList: List<Record?>): HashMap<Int, ArrayList<RecordYear>> {
        val quatersToYearyHashMap: HashMap<Int, ArrayList<RecordYear>> = HashMap()
        var temp: List<String>
        var tempRecordYearList: ArrayList<RecordYear>

        for (record in recordList) {
            if (record != null) {
                temp = record.quarter.split("-")
                if (record.id == 1) {
                    tempRecordYearList = arrayListOf(RecordYear(temp[1], record.volumeOfMobileData))
                    quatersToYearyHashMap[temp[0].toInt()] = tempRecordYearList
                } else {
                    val previousIndex = record.id - 2
                    val previousYear = recordList[previousIndex]?.quarter?.split("-")?.get(0)

                    if (compareValues(previousYear, temp[0]) == 0) {
                        if (previousYear != null) {
                            quatersToYearyHashMap[previousYear.toInt()]?.add(
                                RecordYear(
                                    temp[1],
                                    record.volumeOfMobileData
                                )
                            )
                        }
                    } else {
                        tempRecordYearList =
                            arrayListOf(RecordYear(temp.get(1), record.volumeOfMobileData))
                        quatersToYearyHashMap[temp[0].toInt()] = tempRecordYearList
                    }
                }
            }
        }
        quatersToYearyHashMap.toSortedMap()
        return quatersToYearyHashMap
    }

    fun wasDipInDataUsageInYear(arrayList: ArrayList<RecordYear>): Boolean {
        val sorted: List<Double> = arrayList.map { it.dataUsage }.sorted()
        val orignal: List<Double> = arrayList.map { it.dataUsage }
        return orignal == sorted
    }

    fun getTotalUsageInYear(arrayList: ArrayList<RecordYear>): Double {
        return arrayList.map { it.dataUsage }.sum()
    }
}