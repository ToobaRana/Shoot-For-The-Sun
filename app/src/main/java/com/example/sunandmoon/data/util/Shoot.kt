package com.example.sunandmoon.data.util

import android.location.Location
import java.time.LocalDate
import java.time.LocalDateTime

data class Shoot(
    val id: Int? = null,
    val name: String = "My Shoot",
    val locationName: String = "UiO",
    val location: Location = Location("").apply {
        latitude = 59.943965
        longitude = 10.7178129
    },
    val date: LocalDateTime = LocalDateTime.now().withSecond(0).withNano(0),
    val timeZoneOffset: Double = 2.0,
    val parentProductionId: Int? = null
)
