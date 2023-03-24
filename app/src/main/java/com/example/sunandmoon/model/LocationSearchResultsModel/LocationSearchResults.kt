package com.example.sunandmoon.model.LocationSearchResultsModel

import com.example.sunandmoon.model.SunriseModel.Geometry
import com.example.sunandmoon.model.SunriseModel.Properties
import com.example.sunandmoon.model.SunriseModel.When
import com.google.gson.annotations.SerializedName

data class LocationSearchResults(
    val place_id: Int,
    val licence: String,
    val osm_type: String,
    val osm_id: Int,

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