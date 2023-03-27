package com.example.sunandmoon.model.SunriseModel

import kotlinx.serialization.Serializable

@Serializable
data class When(
    val interval : List<String>
)
