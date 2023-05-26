package com.example.sunandmoon.model.locationSearchResultsModel

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LocationSearchResults(
    val place_id: Long,
    val licence: String,
    val osm_type: String,
    val osm_id: Long,

    val boundingbox: List<String>,
    val lat: String,
    val lon: String,
    val display_name: String,

    @SerializedName("class")
    val classString: String,
    val type: String,
    val importance: Double,
    val icon: String,

    val address: Map<String, String>
)