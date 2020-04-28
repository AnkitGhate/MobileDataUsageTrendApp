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

    fun getAllRecordsFromRepo(): ApiResponse {
        return recordRepository.getAllRecordsFromDB()
    }

    suspend fun getSortedRecordsPerYearList(recordsList: List<Record?>): ArrayList<RecordYear> {
        return recordRepository.sortRecordsBasedOnYear(recordsList)
    }
}


