package com.ankitgh.mobiledatatrend.rest

import com.ankitgh.mobiledatatrend.database.Record

data class Result(
    val resource_id: String,
    val fields: List<Fields>,
    val records: List<Record>,
    val _links: Links,
    val limit: Int,
    val total: Int
)