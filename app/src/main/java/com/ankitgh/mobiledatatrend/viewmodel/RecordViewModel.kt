package com.ankitgh.mobiledatatrend.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ankitgh.mobiledatatrend.database.Record
import com.ankitgh.mobiledatatrend.repository.ApiResponse
import com.ankitgh.mobiledatatrend.repository.RecordRepository
import com.ankitgh.mobiledatatrend.rest.model.RecordYear

class RecordViewModel(application: Application) : AndroidViewModel(application) {

    private var recordRepository: RecordRepository

    init {
        recordRepository = RecordRepository(application)
    }

    /**
     * Gets called from Activity and fetches records from database
     * Note: DB by this time will be updated from network data and if not will fetch from
     * database.
     */
    fun getAllRecordsFromRepo(): ApiResponse {
        return recordRepository.getAllRecordsFromDB()
    }

    /**
     * Takes a list of records which have quaters and are not mapped to Year and returns
     * a mapped list of Records which are sorted based on Year.
     */
    suspend fun getSortedRecordsPerYearList(recordsList: List<Record?>): ArrayList<RecordYear> {
        return recordRepository.sortRecordsBasedOnYear(recordsList)
    }
}


