package com.example.sunandmoon.model.LocationForecastModel

data class Data(
    val instant : Instant,
    val next_1_hours : NextOneHour,
    val next_6_hours : NextSixHours
)
