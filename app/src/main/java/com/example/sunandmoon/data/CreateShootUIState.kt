package com.example.sunandmoon.data

import com.example.sunandmoon.model.LocationSearchResultsModel.LocationSearchResults
import java.time.LocalDateTime

enum class PreferableWeather {
    CLEAR_SKY,
    CLOUDY,
    FAIR,
    RAIN,
    THUNDER,
    SNOW,
}

data class CreateShootUIState (
    val name: String = "My Shoot",
    val locationSearchQuery: String,
    val locationSearchResults: List<LocationSearchResults>,
    val locationEnabled: Boolean,
    val latitude: Double,
    val longitude: Double,
    val chosenDateTime: LocalDateTime,
    val editTimeEnabled: Boolean,
    val timeZoneOffset: Double,
    val parentProductionId: Int? = null,
    val currentShootBeingEditedId: Int? = null,
    val chosenSunPositionIndex: Int,
    val preferredWeather: List<PreferableWeather> = listOf()
)