package com.ankitgh.mobiledatatrend.rest

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtils {
     fun isNetworkAvailable(context: Context): Boolean {
        val service = Context.CONNECTIVITY_SERVICE
        val manager = context.getSystemService(service) as ConnectivityManager?
        val network = manager?.activeNetworkInfo
        return (network != null)
    }
}