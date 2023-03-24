package com.example.sunandmoon.data

import com.example.sunandmoon.model.LocationSearchResultsModel.LocationSearchResults

data class SunUiState(

    val sunRiseTime: String,
    val sunSetTime: String,

    val solarNoon: String,

    val locationSearchResults: List<LocationSearchResults>
)
