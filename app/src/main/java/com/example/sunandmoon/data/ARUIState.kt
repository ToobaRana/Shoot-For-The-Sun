package com.example.sunandmoon.data

import android.location.Location
import java.time.LocalDateTime


data class ARUIState(
    val sunZenith: Double?,
    val sunAzimuth: Double?,
    val chosenDateTime: LocalDateTime,
    val timeZoneOffset: Double,
    val location: Location?,

    val editARSettingsIsOpened: Boolean = false,
    val editTimeEnabled: Boolean = true,
    val chosenSunPositionIndex: Int = 0,

    val hasShownCalibrateMagnetMessage: Boolean = false,
    val hasShownPleaseGiveLocationPermissionMessage: Boolean = false,
    val hasShownMissingSensorsMessage: Boolean = false
)
