package com.example.sunandmoon.data

import com.example.sunandmoon.model.LocationSearchResultsModel.LocationSearchResults
import com.example.sunandmoon.model.SunriseModel.Sunrise3
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*

class DataSource() {

    val baseURLMet: String = "https://api.met.no/weatherapi/sunrise/3.0/"
    val baseURLNominatim: String = "https://nominatim.openstreetmap.org/search"

    private val client = HttpClient() {

    install(ContentNegotiation)
        {
            gson()
        }
    }

    suspend fun fetchSunrise3Data(sunOrMoon: String, lat: Double, lon: Double, date: String, offset: String) : Sunrise3 {
        val endPoint = "$baseURLMet$sunOrMoon?lat=$lat&lon=$lon&date=$date&offset=$offset"

        val apiResults: Sunrise3 = client.get(endPoint).body()

        return apiResults

    }

    // example: https://nominatim.openstreetmap.org/search?q=oslo&format=json&addressdetails=1&limit=10
    suspend fun fetchLocationSearchResults(query: String, limit: Int) : List<LocationSearchResults>{
        //val endPoint = "$baseURLNominatim?q=$query&format=json&addressdetails=1&limit=$limit"
        val endPoint = "https://nominatim.openstreetmap.org/search?q=oslo&format=json&addressdetails=1&limit=10"

        val apiResults: List<LocationSearchResults> = client.get(endPoint).body()

        return apiResults

    }



}