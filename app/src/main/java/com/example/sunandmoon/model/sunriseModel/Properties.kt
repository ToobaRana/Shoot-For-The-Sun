package com.example.sunandmoon.model.sunriseModel

import kotlinx.serialization.Serializable

@Serializable
data class Properties(
    val body: String,
    val sunrise: SunState,
    val sunset: SunState,
    val solarnoon: SolarPoles,
    val solarmidnight: SolarPoles
)




