package com.example.sunandmoon.data

import com.example.sunandmoon.model.LocationSearchResultsModel.LocationSearchResults
import java.time.LocalDateTime

data class ShootInfoUIState(

    val sunriseTime: String,
    val solarNoonTime: String,
    val sunsetTime: String,

    val locationSearchQuery: String,
    val locationSearchResults: List<LocationSearchResults>,
    val locationEnabled: Boolean,
    val longitude: Double,
    val latitude: Double,
    var chosenDate: LocalDateTime,
    val timeZoneOffset: Double
)
