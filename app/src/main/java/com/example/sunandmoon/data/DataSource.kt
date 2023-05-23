package com.example.sunandmoon.data

//import android.R

import android.location.Location
import android.util.Log
import com.example.sunandmoon.BuildConfig
import com.example.sunandmoon.model.LocationForecastModel.LocationForecast
import com.example.sunandmoon.model.LocationSearchResultsModel.LocationSearchResults
import com.example.sunandmoon.model.LocationTimeZoneOffsetResultModel.LocationTimeZoneOffsetResult
import com.example.sunandmoon.model.SunriseModel.Sunrise3
import com.google.android.gms.location.FusedLocationProviderClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*
import org.json.JSONObject


class DataSource() {

    val baseURLMet: String = "https://api.met.no/weatherapi/sunrise/3.0/"
    val baseURLNominatimSearch: String = "https://nominatim.openstreetmap.org/search"
    val baseURLNominatimReverse: String = "https://nominatim.openstreetmap.org/reverse"
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
        val endPoint = "$baseURLNominatimSearch?q=$query&format=json&addressdetails=1&limit=$limit&accept-language=en"

        val apiResults: List<LocationSearchResults> = client.get(endPoint).body()

        return apiResults
    }

    // example: https://nominatim.openstreetmap.org/reverse?lat=59.961266&lon=10.7813993&format=json&zoom=18
    // returns the display name of the given location
    suspend fun fetchReverseGeocoding(location: Location): String {
        val endPoint =
            "$baseURLNominatimReverse?lat=${location.latitude}&lon=${location.longitude}&format=json&accept-language=en&zoom=12"

        val apiResult: String = client.get(endPoint).body()
        val jObject = JSONObject(apiResult)

        return jObject.get("display_name") as String
    }

    // example: https://api.wheretheiss.at/v1/coordinates/59.943965,10.7178129
    suspend fun fetchLocationTimezoneOffset(location: Location): LocationTimeZoneOffsetResult {
        val endPoint = "$baseURLWheretheiss${location.latitude},${location.longitude}"

        val apiResults: LocationTimeZoneOffsetResult = client.get(endPoint).body()

        return apiResults
    }

    //example
    //https://api.met.no/weatherapi/locationforecast/2.0/complete?lat=60.10&lon=10
    suspend fun fetchWeatherAPI(lat : String, lon : String) : LocationForecast{
        val endPoint = "$baseURLLocationForecast?lat=$lat&lon=$lon"

        var apiResults : LocationForecast
        try {
            apiResults = client.get(endPoint){
                headers {
                    header("X-Gravitee-API-Key", API_KEY)
                }
            }.body()
        } catch (e: Exception) {
            throw e
        }

        return apiResults
    }
}


fun fetchLocation(
    fusedLocationClient: FusedLocationProviderClient,
    setCoordinates: (location: Location, setTimeZoneOffset: Boolean) -> Unit
) {
    try {
        Log.i("ararar", "fetch location attempt")
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            Log.i("ararar", "fetch location success")
            if (location != null) {
                setCoordinates(location, true)
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
