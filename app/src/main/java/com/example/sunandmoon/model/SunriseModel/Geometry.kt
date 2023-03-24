package com.example.sunandmoon.model.SunriseModel
import kotlinx.serialization.Serializable

@Serializable
data class Geometry(
    val type : String,
    val coordinates : List<Float>
)
