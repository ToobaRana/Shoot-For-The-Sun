package com.example.sunandmoon.data

import com.example.sunandmoon.model.LocationSearchResultsModel.LocationSearchResults
import java.time.LocalDate

data class SunUiState(

    val sunriseTime: String,
    val solarNoonTime: String,
    val sunsetTime: String,

    val locationSearchQuery: String,
    val locationSearchResults: List<LocationSearchResults>,
    val locationEnabled: Boolean,
    val longitude: Double,
    val latitude: Double,
    var chosenDate: LocalDate,
    val timeZoneOffset: Double
)
