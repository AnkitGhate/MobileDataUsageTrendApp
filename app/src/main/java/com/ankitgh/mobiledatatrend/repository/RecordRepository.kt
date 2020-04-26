package com.ankitgh.mobiledatatrend.repository

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.ankitgh.mobiledatatrend.database.Record
import com.ankitgh.mobiledatatrend.database.RecordDao
import com.ankitgh.mobiledatatrend.database.RecordDatabase
import com.ankitgh.mobiledatatrend.database.RecordDatabase.Companion.dbScope
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

@SuppressLint("LogConditional")
class RecordRepository(application: Application) {
    private val recordDao: RecordDao?
    private val TAG: String = RecordRepository::class.java.simpleName

    init {
        val recordsDataApi: RecordsDataApi = RestConnector.instance

        val recordDatabase: RecordDatabase? = RecordDatabase.getInstance(application)
        recordDao = recordDatabase?.recordDao()

        val dbScope = CoroutineScope(Dispatchers.Default)

        dbScope.launch {
            if (recordDao != null) {
                if (recordDao.checkifAnyRecord() == null) {
                    requestRecordsFromRestService(recordsDataApi)
                }
            }
        }
    }

    fun getAllRecordsFromDB(): LiveData<List<Record>>? = recordDao?.getAllRecords()

    suspend fun insertDataFromServerToDB(recordList: List<Record>) {
        withContext(Dispatchers.IO) {
                recordDao?.insertAllRecords(recordList)
            Log.d(TAG, "insertDataFromServerToDB -> Record DB populated ")
        }
    }

    private fun getRecordsApi(recordsDataApi: RecordsDataApi) = recordsDataApi.getRecords("a807b7ab-6cad-4aa6-87d0-e283a7353a0f", "59")

    private fun requestRecordsFromRestService(recordsDataApi: RecordsDataApi) {
        Log.d(TAG, "Starting to fetch records from WebService...")

        val recordsApi: Call<RecordsBase> = getRecordsApi(recordsDataApi)

        recordsApi.enqueue(object : Callback<RecordsBase> {
            override fun onFailure(call: Call<RecordsBase>, t: Throwable) {
                Log.e(TAG, "Error while fetching data : ${t.message}")
            }

            override fun onResponse(call: Call<RecordsBase>, response: Response<RecordsBase>) {
                val recordBase: RecordsBase? = response.body()
                dbScope.launch {
                    recordBase?.result?.records?.let { insertDataFromServerToDB(it) }
                }
                Log.d(TAG, "Api call success : ${recordBase?.result?.records.toString()}.")
            }
        })
    }
}