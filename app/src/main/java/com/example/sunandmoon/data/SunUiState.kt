package com.example.sunandmoon.data

import com.example.sunandmoon.model.LocationSearchResultsModel.LocationSearchResults

data class SunUiState(

    val sunriseTime: String,
    val solarNoonTime: String,
    val sunsetTime: String,

    val locationSearchQuery: String,
    val locationSearchResults: List<LocationSearchResults>,
    val locationEnabled: Boolean,
    val longitude: Double,
    val latitude: Double,
    var currentDate: Int,
    val currentMonth: Int,
    val timeZoneOffset: Double
)
