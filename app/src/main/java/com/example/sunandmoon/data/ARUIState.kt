package com.example.sunandmoon.data

import android.location.Location
import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.model.LocationForecastModel.LocationForecast
import com.example.sunandmoon.model.LocationSearchResultsModel.LocationSearchResults
import java.time.LocalDateTime

data class ARUIState(
    val sunZenith: Double?,
    val sunAzimuth: Double?,
    val chosenDateTime: LocalDateTime,
    val timeZoneOffset: Double?,
    val location: Location?,

    val editARSettingsIsOpened: Boolean = false,
    val editTimeEnabled: Boolean = true,
    val chosenSunPositionIndex: Int = 0,
)
