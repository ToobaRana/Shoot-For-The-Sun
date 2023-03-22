package com.example.sunandmoon.model
import kotlinx.serialization.Serializable

@Serializable
data class Geometry(
    val type : String,
    val coordinates : List<Float>
)
