package com.example.sunandmoon.util

import android.icu.util.Calendar

fun getTimeZoneOffset(): Double {
    val calendar = Calendar.getInstance()
    val timeZone = calendar.timeZone
    val offsetInMillis = timeZone.getOffset(calendar.timeInMillis)
    val offsetInHours = offsetInMillis.toDouble() / (1000 * 60 * 60)
    return offsetInHours
}