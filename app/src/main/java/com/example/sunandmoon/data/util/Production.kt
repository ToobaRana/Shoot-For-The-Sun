package com.example.sunandmoon.data.util

import android.location.Location
import java.time.LocalDate
import java.time.LocalDateTime

data class Production(
    val name: String = "My Production",
    val shoots: List<Shoot>
)
