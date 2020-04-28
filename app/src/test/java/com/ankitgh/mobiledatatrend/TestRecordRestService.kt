package com.ankitgh.mobiledatatrend

import com.ankitgh.mobiledatatrend.repository.ApiResponse
import com.ankitgh.mobiledatatrend.repository.RecordRepository
import com.ankitgh.mobiledatatrend.rest.RecordsDataApi
import com.ankitgh.mobiledatatrend.rest.RestConnector
import com.google.gson.GsonBuilder
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.net.HttpURLConnection

class TestRecordRestService {
    val testScope = CoroutineScope(Dispatchers.Default)
    val apiResponse: ApiResponse = mock()
    val recordRepository: RecordRepository = Mockito.mock(RecordRepository::class.java)

    @get:Rule
    val mockWebServer = MockWebServer()

    val retrofit by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        Timber.d("Creating new instance of Retrofit")

        Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(RestConnector.getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val recordApi by lazy {
        retrofit.create(RecordsDataApi::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun testDataSourceService() {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(TestUtils.getJson("data_source.json"))
                .setResponseCode(HttpURLConnection.HTTP_OK)
        )
        val response = recordApi.getRecords(pageSize, noOfPages).execute()
        Assert.assertEquals(response.body()?.result, TestUtils.getRecordsTestObject().result)
    }

    @Test
    fun requestsRecordsFromRestServiceTest_404() {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(TestUtils.getJson("data_source.json"))
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        )
        val response = recordApi.getRecords(pageSize, noOfPages).execute()
        Assert.assertEquals(response.code(), 404)
    }
}