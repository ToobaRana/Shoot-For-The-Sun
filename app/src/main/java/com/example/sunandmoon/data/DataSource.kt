package com.example.sunandmoon.data

//import android.R
import android.R
import android.annotation.SuppressLint
import android.provider.Settings.Global.getString
import android.util.Log
import com.example.sunandmoon.BuildConfig
import com.example.sunandmoon.model.LocationForecastModel.LocationForecast

import com.example.sunandmoon.model.LocationSearchResultsModel.LocationSearchResults
import com.example.sunandmoon.model.LocationTimeZoneOffsetResultModel.LocationTimeZoneOffsetResult
import com.example.sunandmoon.model.SunriseModel.Sunrise3
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LastLocationRequest
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
    val baseURLLocationForecast : String = "https://api.met.no/weatherapi/locationforecast/2.0/complete"

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
        val endPoint = "$baseURLNominatim?q=$query&format=json&addressdetails=1&limit=$limit&accept-language=en"

        val apiResults: List<LocationSearchResults> = client.get(endPoint).body()

        return apiResults

    }

    // example: https://api.wheretheiss.at/v1/coordinates/59.943965,10.7178129
    suspend fun fetchLocationTimezoneOffset(latitude: Double, longitude: Double): LocationTimeZoneOffsetResult {
        val endPoint = "$baseURLWheretheiss$latitude,$longitude"

        val apiResults: LocationTimeZoneOffsetResult = client.get(endPoint).body()

        return apiResults
    }

    //example
    //https://api.met.no/weatherapi/locationforecast/2.0/complete?lat=60.10&lon=10
    suspend fun fetchWeatherAPI(lat : String, lon : String) : LocationForecast{
        val endPoint = "$baseURLLocationForecast?lat=$lat&lon=$lon"

        val apiResults : LocationForecast = client.get(endPoint){
            headers {
                header("X-Gravitee-API-Key", API_KEY)
            }
        }
            .body()

        return apiResults
    }
}


fun fetchLocation(
    fusedLocationClient: FusedLocationProviderClient,
    setCoordinates: (latitude: Double, longitude: Double, setTimeZoneOffset: Boolean) -> Unit
) {
    try {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                setCoordinates(location.latitude, location.longitude, true)
            } else {
                Log.d("Location", "Last known location is not available")
                // Handle the case where location is null
                // You can perform additional actions or set default coordinates here
            }
        }
    } catch (e: SecurityException) {
        Log.e("Location", "Location permission is not granted", e)
    } catch (e: Exception) {
        Log.e("Location", "Error fetching last known location: ${e.message}", e)
    }
}
