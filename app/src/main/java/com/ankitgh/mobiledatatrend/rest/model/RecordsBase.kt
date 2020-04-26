package com.ankitgh.mobiledatatrend.rest.model

import com.ankitgh.mobiledatatrend.rest.Result

data class RecordsBase(
    val help: String,
    val success: Boolean,
    val result: Result
)