package com.ankitgh.mobiledatatrend.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Entity to store records of data usage per quarter
 */
@Entity(tableName = "record_table")
data class Record(
    @PrimaryKey
    @SerializedName("_id")
    val id: Int,
    val quarter: String,
    @SerializedName("volume_of_mobile_data")
    val volumeOfMobileData: Double
)