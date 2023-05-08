package com.example.sunandmoon.data

import com.example.sunandmoon.model.LocationSearchResultsModel.LocationSearchResults
import java.time.LocalDateTime

data class CreateShootUIState (
    val name: String = "My Shoot",
    val locationSearchQuery: String,
    val locationSearchResults: List<LocationSearchResults>,
    val locationEnabled: Boolean,
    val latitude: Double,
    val longitude: Double,
    var chosenDate: LocalDateTime,
    val timeZoneOffset: Double,
    val parentProductionId: Int?,
)