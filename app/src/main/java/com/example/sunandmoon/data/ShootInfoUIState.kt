package com.example.sunandmoon.data

import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.model.LocationSearchResultsModel.LocationSearchResults

data class ShootInfoUIState(

    val shoot: Shoot?,

    val sunriseTime: String,
    val solarNoonTime: String,
    val sunsetTime: String
)
