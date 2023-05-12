package com.example.sunandmoon.data.util

import android.location.Location
import java.time.LocalDate
import java.time.LocalDateTime

data class Production(
    val id: Int? = null,
    val name: String = "My Production",
    var duration: Pair<LocalDateTime?, LocalDateTime?> = Pair(null, null)
)
