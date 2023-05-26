package com.example.sunandmoon.data

import com.example.sunandmoon.data.dataUtil.Shoot

data class ShootInfoUIState(

    val shoot: Shoot? = null,
    val sunriseTime: String = "not calculated",
    val solarNoonTime: String = "not calculated",
    val sunsetTime: String = "not calculated",

    val weatherIcon: Int? = null,
    val temperature: Double? = null,
    var rainfallInMm: Double? = null,
    val windSpeed: Double? = null,
    val windDirection: Double? = null,
    val uvIndex: Double? = null,
    val missingNetworkConnection: Boolean = false
)
