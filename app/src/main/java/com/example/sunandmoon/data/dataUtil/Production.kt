package com.example.sunandmoon.data.dataUtil

import java.time.LocalDateTime

data class Production(
    val id: Int? = null,
    val name: String = "My Production",
    var duration: Pair<LocalDateTime?, LocalDateTime?> = Pair(null, null)
)
