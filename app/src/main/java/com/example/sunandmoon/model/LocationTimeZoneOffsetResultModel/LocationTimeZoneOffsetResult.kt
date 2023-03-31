package com.example.sunandmoon.model.LocationTimeZoneOffsetResultModel

import kotlinx.serialization.Serializable

@Serializable
data class LocationTimeZoneOffsetResult(
    val latitude: String,
    val longitude: String,
    val timezone_id: String,
    val offset: Int,
    val country_code: String,
    val map_url: String
)