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

    fun getSortedRecordsPerYearList(recordsList: List<Record?>): ArrayList<RecordYear> {
        val recordHashMap: HashMap<Int, ArrayList<RecordYear>> = mapQuartersToYear(recordsList)
        val recordYearsFinalArrayList: ArrayList<RecordYear> = ArrayList()

        for (recordEntry in recordHashMap.entries) {
            val arrayList: ArrayList<RecordYear> = recordEntry.value
            val totalSumOfUsage: Double = getTotalUsageInYear(arrayList)
            val dipInUsage: Boolean = wasDipInDataUsageInYear(arrayList)
            recordYearsFinalArrayList.add(RecordYear(recordEntry.key.toString(), totalSumOfUsage, dipInUsage))
        }
        recordYearsFinalArrayList.sortBy { it.quater }
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
                            quatersToYearyHashMap[previousYear.toInt()]?.add(RecordYear(temp[1], record.volumeOfMobileData))
                        }
                    } else {
                        tempRecordYearList = arrayListOf(RecordYear(temp.get(1), record.volumeOfMobileData))
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


