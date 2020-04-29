package com.ankitgh.mobiledatatrend.database

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import timber.log.Timber

/**
 * Database class to init ROOM and return instance
 */
@Database(entities = [Record::class], version = 1)
abstract class RecordsDatabase : RoomDatabase() {

    abstract fun recordDao(): RecordDao

    companion object {
        @Synchronized
        fun getInstance(context: Context): RecordsDatabase {
            Timber.d("getInstance : Record DB initialized")
            return Room.databaseBuilder(
                context.applicationContext,
                RecordsDatabase::class.java, "record_database")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}