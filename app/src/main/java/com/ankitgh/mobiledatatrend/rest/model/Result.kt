package com.ankitgh.mobiledatatrend.rest.model

import com.ankitgh.mobiledatatrend.database.Record
import com.google.gson.annotations.SerializedName

data class Result(
    val resource_id: String,
    val fields: List<Fields>,
    val records: List<Record>,
    @SerializedName("_links")
    val links: Links,
    val limit: Int,
    val total: Int
)