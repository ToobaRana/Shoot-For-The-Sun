package com.example.sunandmoon.data.util

import android.location.Location
import java.time.LocalDate
import java.time.LocalDateTime

data class Shoot(
    val name: String = "my production",
    val locationName: String = "UiO",
    val location: Location = Location("").apply {
        latitude = 59.943965
        longitude = 10.7178129
    },
    val date: LocalDateTime = LocalDateTime.now(),
    val timeZoneOffset: Double = 2.0
)
