package com.ankitgh.mobiledatatrend.repository

import retrofit2.HttpException
import java.net.UnknownHostException

fun getErrorMessage(error: Throwable): String {
    return when (error) {
        is HttpException -> "There is a problem refreshing data. [ ERROR - ${error.code()} : ${error.message()}"
        is UnknownHostException -> "There is a problem refreshing data. [ ERROR : ${error.message}"
        else -> "There is a problem refreshing data. Please check network connection."
    }
}