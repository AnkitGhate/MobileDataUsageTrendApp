package com.ankitgh.mobiledatatrend.repository

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.ankitgh.mobiledatatrend.database.Record
import com.ankitgh.mobiledatatrend.database.RecordDao
import com.ankitgh.mobiledatatrend.database.RecordsDatabase
import com.ankitgh.mobiledatatrend.noOfPages
import com.ankitgh.mobiledatatrend.pageSize
import com.ankitgh.mobiledatatrend.rest.RecordsDataApi
import com.ankitgh.mobiledatatrend.rest.RestConnector
import com.ankitgh.mobiledatatrend.rest.model.RecordsBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repository to handle data from server and DB and decide based
 * on availability as to which datasource to choose and propagate
 * data and errors to ViewModels accordingly
 */
@SuppressLint("LogConditional")
class RecordRepository(application: Application) {

    private val TAG: String = RecordRepository::class.java.simpleName
    private var recordDao: RecordDao
    private var apiResponse: ApiResponse = ApiResponse()
    private val recordsDatabase: RecordsDatabase
    private val repoScope = CoroutineScope(Dispatchers.Default)

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

    suspend fun insertDataFromServerToDB(recordList: List<Record>) {
        withContext(Dispatchers.IO) {
            recordDao.insertAllRecords(recordList)
            Log.d(TAG, "insertDataFromServerToDB -> Record DB populated ")
        }
    }

    /**
     * Fetches data from rest service and supplies upper models with errors and
     * refreshes Database
     */
    private fun requestRecordsFromRestService(recordsDataApi: RecordsDataApi): ApiResponse {
        Log.d(TAG, "Starting to fetch records from WebService...")

        val recordsApi: Call<RecordsBase> = recordsDataApi.getRecords(pageSize, noOfPages)

        recordsApi.enqueue(object : Callback<RecordsBase> {
            override fun onFailure(call: Call<RecordsBase>, error: Throwable) {
                apiResponse.apiError.value = getErrorMessage(error)
                Log.e(TAG, "Error while fetching data : ${error.message}")
            }

            override fun onResponse(call: Call<RecordsBase>, response: Response<RecordsBase>) {
                val recordBase: RecordsBase? = response.body()
                repoScope.launch {
                    if (response.isSuccessful) {
                        recordBase?.result?.records?.let { insertDataFromServerToDB(it) }
                    } else {
                        apiResponse.apiError.value = "There is a problem refreshing data from API"
                    }
                }
                Log.d(TAG, "Api call success : ${recordBase?.result?.records.toString()}.")
            }
        })
        return apiResponse
    }
}