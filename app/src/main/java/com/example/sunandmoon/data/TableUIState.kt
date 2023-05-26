package com.example.sunandmoon.data

import android.location.Location
import com.example.sunandmoon.model.locationSearchResultsModel.LocationSearchResults
import java.time.LocalDate
import java.time.LocalDateTime

data class TableUIState(
    val apiDateTableList: List<String?>,
    val calculationsDateTableList: List<String>,

    val locationSearchQuery: String,
    val locationSearchResults: List<LocationSearchResults>,
    val location: Location,

    val chosenDate: LocalDateTime,
    val chosenSunType: String,

    val timeZoneOffset: Double,
    val timezone_id: String,
    val offsetStringForApi: String,
    val timeZoneListTableScreen: List<String>,

    val setSameDaysFromJanuaryList: List<LocalDate>,

    val missingNetworkConnection: Boolean = false
)