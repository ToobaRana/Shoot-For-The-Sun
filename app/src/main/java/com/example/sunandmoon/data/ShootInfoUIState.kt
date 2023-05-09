package com.example.sunandmoon.data

import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.model.LocationSearchResultsModel.LocationSearchResults

data class ShootInfoUIState(

    val shoot: Shoot? = null,

    val sunriseTime: String = "not calculated",
    val solarNoonTime: String = "not calculated",
    val sunsetTime: String = "not calculated"
)
