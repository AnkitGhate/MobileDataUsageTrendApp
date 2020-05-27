package com.ankitgh.mobiledatatrend.rest.model

data class RecordYear(val quater: String, val dataUsage: Double, val dipInUsage: Boolean = false) {

    fun convertToBigDecimal(dataUsage: Double): String{
        return dataUsage.toBigDecimal().toPlainString()
    }
}

