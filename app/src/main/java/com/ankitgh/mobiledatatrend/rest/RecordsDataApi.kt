package com.ankitgh.mobiledatatrend.rest

import com.ankitgh.mobiledatatrend.rest.model.RecordsBase
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RecordsDataApi {

    /**
     * API is used to return mobile data usage per quarter
     */
    @GET("api/action/datastore_search")
    fun getRecords(
        @Query("resource_id") pageSize: String,
        @Query("limit") currentPage: String
    ): Call<RecordsBase>

}