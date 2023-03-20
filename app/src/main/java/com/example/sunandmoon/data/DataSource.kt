package com.example.sunandmoon.data

import com.example.sunandmoon.model.Sunrise3
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*

class DataSource() {

    val baseURL: String = "https://api.met.no/weatherapi/sunrise/3.0/"

    private val client = HttpClient() {

    install(ContentNegotiation)
        {
            gson()
        }
    }

    suspend fun fetchSunrise3Data(sunOrMoon: String, lat: Double, lon: Double, date: String, offset: String) : Sunrise3{
        val endPoint = "$baseURL$sunOrMoon?lat=$lat&lon=$lon&date=$date&offset=$offset"

        val apiResults: Sunrise3 = client.get(endPoint).body()

        return apiResults

    }



}