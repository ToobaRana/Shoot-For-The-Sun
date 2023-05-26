package com.example.sunandmoon.util

import android.icu.util.Calendar
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

//gets timezoneOffset for location
fun getCurrentTimeZoneOffset(): Double {
    val calendar = Calendar.getInstance()
    val timeZone = calendar.timeZone
    val offsetInMillis = timeZone.getOffset(calendar.timeInMillis)
    return offsetInMillis.toDouble() / (1000 * 60 * 60)
}

//find offset based on the location id and date
fun findTimeZoneOffsetOfDate(location: String, date: String): Double {
    val dateTime =
        LocalDateTime.of(convertStringToLocalDate(date), LocalDateTime.now().toLocalTime())
    val zoneId = ZoneId.of(location)
    val zoneOffset = zoneId.rules.getOffset(dateTime)

    val offsetHours = zoneOffset.totalSeconds / 3600
    val offsetMinutes = (zoneOffset.totalSeconds % 3600) / 60

    return offsetHours + (offsetMinutes.toDouble() / 60)
}

private fun convertStringToLocalDate(dateString: String): LocalDate {

    return LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE)
}