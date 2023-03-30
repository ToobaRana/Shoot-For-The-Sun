package com.example.sunandmoon.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.SunUiState
import com.example.sunandmoon.fetchLocation
import com.example.sunandmoon.getSunRiseNoonFall
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

class SunViewModel : ViewModel() {

    private val sunDataSource = DataSource()

    //sunDataSource.fetchSunrise3Data("sun", 59.933333, 10.716667, "2022-12-18", "+01:00" ).properties.sunrise.time
    private val _sunUiState = MutableStateFlow(
        SunUiState(
            sunriseTime = "not calculated",
            solarNoonTime = "not calculated",
            sunsetTime = "not calculated",
            locationSearchQuery = "UiO",
            locationSearchResults = listOf(),
            locationEnabled = true,
            latitude = 59.943965,
            longitude = 10.7178129,
            currentDate = 0,
            currentMonth = 0
        )
    )

    val sunUiState: StateFlow<SunUiState> = _sunUiState.asStateFlow()

    init {
        //loadSunInformation()
        //setCoordinates(item.lat.toDouble(), item.lon.toDouble())
        val sunTimes = getSunRiseNoonFall(Instant.now().toString(), sunUiState.value.latitude, sunUiState.value.longitude)
        setSolarTimes(sunTimes[0], sunTimes[1], sunTimes[2])
    }

    private fun loadSunInformation() {
        viewModelScope.launch {
            try {
                val sunRiseTime = sunDataSource.fetchSunrise3Data(
                    "sun",
                    59.933333,
                    10.716667,
                    "2022-12-18",
                    "+01:00"
                ).properties.sunrise.time

                val sunSetTime = sunDataSource.fetchSunrise3Data(
                    "sun",
                    59.933333,
                    10.716667,
                    "2022-12-18",
                    "+01:00"
                ).properties.sunset.time

                val solarNoon = sunDataSource.fetchSunrise3Data(
                    "sun",
                    59.933333,
                    10.716667,
                    "2022-12-18",
                    "+01:00"
                ).properties.solarnoon.time

                _sunUiState.update { currentState ->
                    currentState.copy(
                        sunriseTime = sunRiseTime,
                        sunsetTime = sunSetTime,
                        solarNoonTime = solarNoon,
                        locationSearchResults = listOf(),
                    )
                }

                Log.d(
                    "test",
                    sunUiState.value.sunriseTime + sunUiState.value.sunsetTime + sunUiState.value.solarNoonTime
                )
            } catch (e: Throwable) {
                Log.d("error", "uh oh" + e.toString())
            }
        }
    }

    fun setLocationSearchQuery(inputQuery: String) {
        _sunUiState.update { currentState ->
            currentState.copy(
                locationSearchQuery = inputQuery
            )
        }
    }

    fun loadLocationSearchResults(query: String) {
        viewModelScope.launch {
            try {
                val locationSearchResults = sunDataSource.fetchLocationSearchResults(query, 10)

                _sunUiState.update { currentState ->
                    currentState.copy(
                        locationSearchResults = locationSearchResults
                    )
                }

            } catch (e: Throwable) {
                Log.d("error", "uh oh" + e.toString())
            }
        }
    }

    fun updateLocation(newValue: Boolean) {
        _sunUiState.update { currentState ->
            currentState.copy(
                locationEnabled = newValue
            )
        }
    }

    fun setCoordinates(latitude: Double, longitude: Double) {
        _sunUiState.update { currentState ->
            currentState.copy(
                latitude = latitude,
                longitude = longitude
            )
        }
    }

    //calls fetchLocation method with provider client, then updates latitude and longitude in uiState with return value
    fun getCurrentPosition(fusedLocationProviderClient: FusedLocationProviderClient) {
        viewModelScope.launch() {
            val location = fetchLocation(fusedLocationProviderClient)
            if (location != null) {
                setCoordinates(location.first, location.second)
            }
        }
    }
    fun setNewDate(newDate: Int){
        _sunUiState.update { currentState ->
            currentState.copy(
                currentDate = newDate
            )
        }
    }
    fun updateMonth(newMonth: Int){
        _sunUiState.update { currentState ->
            currentState.copy(
                currentMonth = newMonth
            )
        }
    }

    fun setSolarTimes(sunriseTime: String, solarNoonTime: String, sunsetTime: String) {
        _sunUiState.update { currentState ->
            currentState.copy(
                sunriseTime = sunriseTime,
                solarNoonTime = solarNoonTime,
                sunsetTime = sunsetTime
            )
        }
    }

}
