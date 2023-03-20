package com.example.sunandmoon.model
import kotlinx.serialization.Serializable

@Serializable
data class SolarPoles(
    val time : String,
    val disc_centre_elevation : Float,
    val visible : Boolean
)
