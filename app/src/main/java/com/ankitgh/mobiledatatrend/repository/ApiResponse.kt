package com.ankitgh.mobiledatatrend.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ankitgh.mobiledatatrend.database.Record

data class ApiResponse(var data: LiveData<List<Record>>? = null, var apiError: MutableLiveData<String> = MutableLiveData())
