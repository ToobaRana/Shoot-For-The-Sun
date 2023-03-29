package com.example.sunandmoon.data

import com.example.sunandmoon.model.LocationSearchResultsModel.LocationSearchResults

data class SunUiState(

    val sunRiseTime: String,
    val sunSetTime: String,

    val solarNoon: String,

    val locationSearchResults: List<LocationSearchResults>,
    val locationEnabled: Boolean,
    val longitude: Double,
    val latitude: Double,
    var currentDate: Int,
    val currentMonth: Int
)
