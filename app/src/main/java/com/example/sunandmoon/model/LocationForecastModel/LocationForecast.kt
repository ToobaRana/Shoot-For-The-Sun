package com.example.sunandmoon.model.LocationForecastModel



data class LocationForecast (
    val type : String,
    val geometry : Geometry,
    val properties: Properties
    )