package com.example.sunandmoon.model.SunriseModel

import kotlinx.serialization.Serializable

@Serializable
data class Properties(
    val body: String,
    val sunrise: SunState,
    val sunset: SunState,
    val solarnoon: SolarPoles,
    val solarmidnight: SolarPoles
)
