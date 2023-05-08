package com.example.sunandmoon.model.LocationForecastModel

import android.telecom.Call.Details

data class NextOneHour(
    val summary : Summary,
    val details : Details
)
