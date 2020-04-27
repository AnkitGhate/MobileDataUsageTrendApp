package com.ankitgh.mobiledatatrend

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.ankitgh.mobiledatatrend.database.RecordsDatabase
import com.ankitgh.mobiledatatrend.viewmodel.RecordViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.*

class TestRecordRepository {
    private lateinit var recordViewModel: RecordViewModel
    private lateinit var recordsDatabase: RecordsDatabase

    @get:Rule
    var instantTaskExecutionRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val testScope = CoroutineScope(Dispatchers.Default)
        recordViewModel = RecordViewModel(context.applicationContext as Application)
        recordsDatabase = Room.inMemoryDatabaseBuilder(context, RecordsDatabase::class.java).build()

        testScope.launch { addDummyDataToDB(recordsDatabase) }
    }

    @After
    fun tearDown() {
        recordsDatabase.close()
    }

    @Test
    fun testGetAllRecordsFromDB() {
        val recordDao = recordsDatabase.recordDao()
        Assert.assertEquals(quaterlyRecords, getValue(recordDao.getAllRecords()))
    }

}