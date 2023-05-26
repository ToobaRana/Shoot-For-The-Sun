package com.example.sunandmoon.model.locationForecastModel



data class LocationForecast (
    val type : String,
    val geometry : Geometry,
    val properties: Properties
    )