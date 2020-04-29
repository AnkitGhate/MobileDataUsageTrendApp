package com.ankitgh.mobiledatatrend.repository

import retrofit2.HttpException
import java.net.UnknownHostException

/**
 * Utility class for Repository
 */

/**
 * Intakes a throwable and returns a string message for the respective exception
 * that has been received.
  */
fun getErrorMessage(error: Throwable): String {
    return when (error) {
        is HttpException -> "There is a problem refreshing data. [ ERROR - ${error.code()} : ${error.message()}"
        is UnknownHostException -> "There is a problem refreshing data. [ ERROR : ${error.message}"
        else -> "There is a problem refreshing data. Please check network connection."
    }
}