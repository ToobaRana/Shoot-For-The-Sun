package com.example.sunandmoon.data

//import android.R
import android.R
import android.provider.Settings.Global.getString
import com.example.sunandmoon.BuildConfig

import com.example.sunandmoon.model.LocationSearchResultsModel.LocationSearchResults
import com.example.sunandmoon.model.LocationTimeZoneOffsetResultModel.LocationTimeZoneOffsetResult
import com.example.sunandmoon.model.SunriseModel.Sunrise3
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.gson.*


class DataSource() {

    val baseURLMet: String = "https://api.met.no/weatherapi/sunrise/3.0/"
    val baseURLNominatim: String = "https://nominatim.openstreetmap.org/search"
    val baseURLWheretheiss: String = "https://api.wheretheiss.at/v1/coordinates/"


    private val client = HttpClient() {

    install(ContentNegotiation)
        {
            gson()
        }
    }

    val API_KEY = BuildConfig.API_KEY



    suspend fun fetchSunrise3Data(sunOrMoon: String, lat: Double, lon: Double, date: String, offset: String): Sunrise3 {
        val endPoint = "$baseURLMet$sunOrMoon?lat=$lat&lon=$lon&date=$date&offset=$offset"

        val apiResults: Sunrise3 = client.get(endPoint) {
            headers {
                header("X-Gravitee-API-Key", API_KEY)
            }
        }
            .body()

        return apiResults

    }

    // example: https://nominatim.openstreetmap.org/search?q=oslo&format=json&addressdetails=1&limit=10
    suspend fun fetchLocationSearchResults(query: String, limit: Int): List<LocationSearchResults>{
        val endPoint = "$baseURLNominatim?q=$query&format=json&addressdetails=1&limit=$limit"

        val apiResults: List<LocationSearchResults> = client.get(endPoint).body()

        return apiResults

    }

    // example: https://api.wheretheiss.at/v1/coordinates/59.943965,10.7178129
    suspend fun fetchLocationTimezoneOffset(latitude: Double, longitude: Double): LocationTimeZoneOffsetResult {
        val endPoint = "$baseURLWheretheiss$latitude,$longitude"

        val apiResults: LocationTimeZoneOffsetResult = client.get(endPoint).body()

        return apiResults
    }
}