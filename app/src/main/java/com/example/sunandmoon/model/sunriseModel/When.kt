package com.example.sunandmoon.model.sunriseModel

import kotlinx.serialization.Serializable

@Serializable
data class When(
    val interval : List<String>
)
