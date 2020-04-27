package com.ankitgh.mobiledatatrend

import com.ankitgh.mobiledatatrend.rest.model.RecordsBase
import com.google.gson.Gson
import java.io.File

class TestUtils {
    companion object {
        fun getJson(path: String): String {
            val url = this::class.java.classLoader?.getResource(path)
            if (url != null) {
                val file = File(url.path)
                return String(file.readBytes())
            }
            return ""
        }

        fun getRecordsTestObject(): RecordsBase {
            val gson = Gson()
            return gson.fromJson(getJson("data_source.json"), RecordsBase::class.java)
        }
    }
}