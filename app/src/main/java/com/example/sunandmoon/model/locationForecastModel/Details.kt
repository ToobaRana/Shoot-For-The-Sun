package com.example.sunandmoon.model.locationForecastModel

data class Details(
    val air_temperature: Double,
    val wind_from_direction: Double,
    val wind_speed: Double,
    val precipitation_amount: Double? = null,
    val ultraviolet_index_clear_sky : Double

)
