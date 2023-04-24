package com.example.sunandmoon.data

import java.time.LocalDate

data class TableUIState(
    val apiDateTableList: List<String>,
    val calculationsDateTableList: List<String>,
    val latitude: Double,
    val longitude: Double,
    val chosenDate: LocalDate,
    val chosenSunType: String,
    val timeZoneOffset: Double
)