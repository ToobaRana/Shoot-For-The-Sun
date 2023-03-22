package com.example.sunandmoon.model

import kotlinx.serialization.Serializable

@Serializable
data class When(
    val interval : List<String>
)
