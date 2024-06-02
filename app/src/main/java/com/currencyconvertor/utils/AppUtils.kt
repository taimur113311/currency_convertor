package com.currencyconvertor.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object AppUtils {
    fun getLastThreeDates(): List<String> {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        val dates = mutableListOf<String>()
        for (i in 0..2) {
            dates.add(formatter.format(calendar.time))
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }

        return dates
    }
}