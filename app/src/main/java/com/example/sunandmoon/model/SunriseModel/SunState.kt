package com.example.sunandmoon.model.SunriseModel

import kotlinx.serialization.Serializable

@Serializable
data class SunState(
    val time : String?,
    val azimuth : Float
)
