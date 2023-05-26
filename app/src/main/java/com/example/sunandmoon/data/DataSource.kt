package com.example.sunandmoon.data

//import android.R

import android.location.Location
import com.example.sunandmoon.BuildConfig
import com.example.sunandmoon.model.locationForecastModel.LocationForecast
import com.example.sunandmoon.model.locationSearchResultsModel.LocationSearchResults
import com.example.sunandmoon.model.locationTimeZoneOffsetResultModel.LocationTimeZoneOffsetResult
import com.example.sunandmoon.model.sunriseModel.Sunrise3
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*
import org.json.JSONObject


class DataSource {

    private val baseURLMet: String = "https://api.met.no/weatherapi/sunrise/3.0/"
    private val baseURLNominatimSearch: String = "https://nominatim.openstreetmap.org/search"
    private val baseURLNominatimReverse: String = "https://nominatim.openstreetmap.org/reverse"
    private val baseURLWheretheiss: String = "https://api.wheretheiss.at/v1/coordinates/"
    private val baseURLLocationForecast : String = "https://api.met.no/weatherapi/locationforecast/2.0/complete"

    private val client = HttpClient {

        install(ContentNegotiation)
        {
            gson()
        }
    }

    private val API_KEY = BuildConfig.API_KEY //could be changed to lowercase, but looks cooler uppercase


    //example: "https://api.met.no/weatherapi/sunrise/3.0/sun?lat=60.10&lon=9.58&date=2015-01-14&offset=+01:00"
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
    suspend fun fetchLocationSearchResults(query: String, limit: Int): List<LocationSearchResults> {
        val endPoint =
            "$baseURLNominatimSearch?q=$query&format=json&addressdetails=1&limit=$limit&accept-language=en"
        //returns api-results
        return client.get(endPoint).body()
    }

    // example: https://nominatim.openstreetmap.org/reverse?lat=59.961266&lon=10.7813993&format=json&zoom=18
    // returns the display name of the given location
    suspend fun fetchReverseGeocoding(location: Location): String {
        val endPoint =
            "$baseURLNominatimReverse?lat=${location.latitude}&lon=${location.longitude}&format=json&accept-language=en&zoom=12"

        val apiResult: String = client.get(endPoint).body()
        val jObject = JSONObject(apiResult)
        //returns api-results
        return jObject.get("display_name") as String
    }

    // example: https://api.wheretheiss.at/v1/coordinates/59.943965,10.7178129
    suspend fun fetchLocationTimezoneOffset(location: Location): LocationTimeZoneOffsetResult {
        val endPoint = "$baseURLWheretheiss${location.latitude},${location.longitude}"
        //returns api-results:
        return client.get(endPoint).body()
    }

    //example
    //https://api.met.no/weatherapi/locationforecast/2.0/complete?lat=60.10&lon=10
    suspend fun fetchWeatherAPI(lat : String, lon : String) : LocationForecast{
        val endPoint = "$baseURLLocationForecast?lat=$lat&lon=$lon"

        val apiResults : LocationForecast
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
