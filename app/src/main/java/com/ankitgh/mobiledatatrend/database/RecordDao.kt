package com.ankitgh.mobiledatatrend.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecordDao {
    @Insert
    fun insertRecord(record: Record)

    @Query("SELECT * FROM record_table")
    fun getAllRecords(): LiveData<List<Record>>

    @Query("SELECT * FROM record_table LIMIT 1")
    fun checkIfAnyRecordAvailable(): Record?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRecords(record: List<Record>)
}