package com.example.sunandmoon.model.locationTimeZoneOffsetResultModel

import kotlinx.serialization.Serializable

@Serializable
data class LocationTimeZoneOffsetResult(
    val latitude: String,
    val longitude: String,
    val timezone_id: String,
    val offset: Double,
    val country_code: String,
    val map_url: String
)