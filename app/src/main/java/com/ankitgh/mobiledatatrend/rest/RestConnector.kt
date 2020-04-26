package com.ankitgh.mobiledatatrend.rest

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestConnector {
    private const val BASE_URL: String = "https://data.gov.sg/"
    private val TAG: String = RestConnector::class.java.simpleName

    val instance: RecordsDataApi by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        Log.d(TAG, "Creating new instance of Retrofit")

        retrofit.create(RecordsDataApi::class.java)
    }

    fun getOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

}