package com.ankitgh.mobiledatatrend.rest

import androidx.lifecycle.LiveData
import com.ankitgh.mobiledatatrend.database.Record

interface ApiResponseCallBack {
    fun onSuccess(recordLiveData: LiveData<List<Record>>)
    fun onError()
}