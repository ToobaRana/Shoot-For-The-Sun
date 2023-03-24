package com.example.sunandmoon.model.SunriseModel

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Sunrise3(
    val copyright : String,
    val licenseURL : String,
    val type : String,
    val geometry: Geometry,

    @SerializedName("when")
    val intervalTime : When,

    val properties: Properties
)
