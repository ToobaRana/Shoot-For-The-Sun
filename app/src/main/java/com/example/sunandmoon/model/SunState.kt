package com.example.sunandmoon.model

import kotlinx.serialization.Serializable

@Serializable
data class SunState(
    val time : String,
    val azimuth : Float
)
