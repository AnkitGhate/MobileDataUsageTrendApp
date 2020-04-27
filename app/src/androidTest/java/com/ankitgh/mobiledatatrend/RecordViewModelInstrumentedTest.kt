package com.ankitgh.mobiledatatrend

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.ankitgh.mobiledatatrend.database.Record
import com.ankitgh.mobiledatatrend.database.RecordsDatabase
import com.ankitgh.mobiledatatrend.rest.model.RecordYear
import com.ankitgh.mobiledatatrend.viewmodel.RecordViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.*

class RecordViewModelInstrumentedTest {

    private lateinit var recordViewModel: RecordViewModel
    private lateinit var recordsDatabase: RecordsDatabase
    private var listOfRecordYear: MutableLiveData<List<Record>> = MutableLiveData()

    @get:Rule
    var instantTaskExecutionRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val repoScope = CoroutineScope(Dispatchers.Default)
        recordViewModel = RecordViewModel(context.applicationContext as Application)
        recordsDatabase = Room.inMemoryDatabaseBuilder(context, RecordsDatabase::class.java).build()
        //listOfRecordYear.value = recordsPerQuarter
        repoScope.launch {
            addDummyDataToDB(recordsDatabase)
        }
    }

    @After
    fun tearDown() {
        recordsDatabase.close()
    }

    @Test
    fun getAllRecordsFromRepoTest() {
        val firstdata: MutableLiveData<List<Record>> = listOfRecordYear
        //val secondDate: MutableLiveData<List<Record>> = recordViewModel.getAllRecordsFromRepo(this) as MutableLiveData<List<Record>>

        //Assert.assertEquals(firstdata, secondDate)
    }

    @Test
    fun getSortedRecordsPerYearListTest() {
        val quarterlyRecordsList: ArrayList<Record> = ArrayList()
        quarterlyRecordsList.addAll(quaterlyRecords)
        Assert.assertEquals(yearlyRecords, recordViewModel.getSortedRecordsPerYearList(quarterlyRecordsList))
    }

    @Test
    fun getTotalUsageInYearTest() {
        val listOfRecordYear = ArrayList<RecordYear>(yearlyRecords)
        Assert.assertTrue(listOfDataUsage.sum() == recordViewModel.getTotalUsageInYear(listOfRecordYear))
    }

}
