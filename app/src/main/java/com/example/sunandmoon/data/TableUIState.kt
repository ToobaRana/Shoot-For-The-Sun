package com.example.sunandmoon.data

data class TableUIState(
    val apiDateTableList: List<String>,
    val calculationsDateTableList: List<String>,
    val latitude: Double,
    val longitude: Double,
    val date: String,
    val chosenSunType: String,
    val timeZoneOffset: Double
    )