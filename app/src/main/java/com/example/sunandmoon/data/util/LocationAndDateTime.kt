package com.example.sunandmoon.data.util

import android.location.Location
import java.time.LocalDateTime

data class LocationAndDateTime (
    val location: Location,
    val dateTime: LocalDateTime
)