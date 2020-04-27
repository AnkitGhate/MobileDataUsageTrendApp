package com.ankitgh.mobiledatatrend

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.ankitgh.mobiledatatrend.database.Record
import com.ankitgh.mobiledatatrend.database.RecordDao
import com.ankitgh.mobiledatatrend.database.RecordsDatabase
import com.ankitgh.mobiledatatrend.rest.model.RecordYear
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

val yearlyRecords = listOf(
    RecordYear("2008", 2.0507370000000003, true),
    RecordYear("2009", 0.746697, true),
    RecordYear("2010", 0.439655, true),
    RecordYear("2018", 0.171586, true)
)

val listOfDataUsage = listOf(2.0507370000000003, 0.746697, 0.439655, 0.171586)

/**
 * Record(2, "2008-Q2", 0.683579),
Record(3, "2008-Q3", 0.683579),
Record(4, "2009-Q1", 0.248899),
Record(5, "2009-Q2", 0.248899),
Record(6, "2009-Q3", 0.248899),
Record(7, "2010-Q4", 0.439655),
Record(8, "2018-Q1", 0.171586)
 */
val quaterlyRecords = listOf(
    Record(1, "2008-Q1", 0.683579)
)

/**
 * recordDao?.insertRecord(Record(16, "2008-Q2", 0.248899))
recordDao?.insertRecord(Record(17, "2008-Q3", 0.439655))
recordDao?.insertRecord(Record(18, "2008-Q4", 0.683579))
 */
suspend fun addDummyDataToDB(recordsDatabase: RecordsDatabase) {
    withContext(Dispatchers.IO) {
        val recordDao: RecordDao? = recordsDatabase.recordDao()
        recordDao?.insertAllRecords(quaterlyRecords)

    }
}

@Throws(InterruptedException::class)
fun <T> getValue(liveData: LiveData<T>): T {
    val data = arrayOfNulls<Any>(1)
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(t: T?) {
            data[0] = t
            latch.countDown()
            liveData.removeObserver(this)
        }
    }
    liveData.observeForever(observer)
    latch.await(2, TimeUnit.SECONDS)
    return data[0] as T
}
