package com.ankitgh.mobiledatatrend.database

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@SuppressLint("SyntheticAccessor")
@Database(entities = [Record::class], version = 1)
abstract class RecordsDatabase : RoomDatabase() {

    abstract fun recordDao(): RecordDao

    companion object {
        private val TAG: String = RecordsDatabase::class.java.simpleName

        @Synchronized
        fun getInstance(context: Context): RecordsDatabase {
            Log.e(TAG, "getInstance : Record DB initialized")
            return Room.databaseBuilder(
                context.applicationContext,
                RecordsDatabase::class.java, "record_database")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}