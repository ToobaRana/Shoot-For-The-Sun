package com.example.sunandmoon.data.dataUtil

import java.time.LocalDateTime

//used for runtime-storage of production-information
data class Production(
    val id: Int? = null,
    val name: String = "My Production",
    var duration: Pair<LocalDateTime?, LocalDateTime?> = Pair(null, null)
)
