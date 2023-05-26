package com.example.sunandmoon.data.dataUtil

import android.location.Location
import com.example.sunandmoon.data.PreferableWeather
import java.time.LocalDateTime

//used for runtime-storage of productions
data class Shoot(
    val id: Int? = null,
    val name: String = "My Shoot",
    val locationName: String = "UiO",
    val location: Location = Location("").apply {
        latitude = 59.943965
        longitude = 10.7178129
    },
    val dateTime: LocalDateTime = LocalDateTime.now().withSecond(0).withNano(0),
    val timeZoneOffset: Double = 2.0,
    val parentProductionId: Int? = null,
    val preferredWeather: List<PreferableWeather>,
    val weatherMatchesPreferences: Boolean? = null
)
