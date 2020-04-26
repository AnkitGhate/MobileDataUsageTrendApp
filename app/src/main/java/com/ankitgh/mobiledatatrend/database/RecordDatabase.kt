package com.ankitgh.mobiledatatrend.database

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@SuppressLint("SyntheticAccessor")
@Database(entities = [Record::class], version = 1)
abstract class RecordDatabase : RoomDatabase() {

    abstract fun recordDao(): RecordDao

    companion object {
        private val TAG: String = RecordDatabase::class.java.simpleName
        private var INSTANCE: RecordDatabase? = null

        val dbScope = CoroutineScope(Dispatchers.Default)

        @Synchronized
        fun getInstance(context: Context): RecordDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    RecordDatabase::class.java, "record_database")
                    .fallbackToDestructiveMigration()
                    .build()
                Log.e(TAG, "getInstance : Record DB initialized")
            }
            return INSTANCE
        }

    }
}