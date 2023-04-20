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
import java.time.LocalDate

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
            chosenDate = LocalDate.now(),
            timeZoneOffset = 2.0
        )
    )

    val sunUiState: StateFlow<SunUiState> = _sunUiState.asStateFlow()

    init {
        //loadSunInformation()
        //setCoordinates(item.lat.toDouble(), item.lon.toDouble())
        //Instant.now().toString()
        val sunTimes = getSunRiseNoonFall(sunUiState.value.chosenDate.toString()+"T12:00:00.000Z", sunUiState.value.timeZoneOffset, sunUiState.value.latitude, sunUiState.value.longitude)
        setSolarTimes(sunTimes[0], sunTimes[1], sunTimes[2])
    }

    private fun loadSunInformation() {
        viewModelScope.launch {
            try {
                val sunRiseTime = sunDataSource.fetchSunrise3Data(
                    "sun",
                    59.943965,
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

    fun setCoordinates(latitude: Double, longitude: Double, setTimeZoneOffset: Boolean) {
        viewModelScope.launch {
            if(setTimeZoneOffset) {
                val locationTimeZoneOffsetResult = sunDataSource.fetchLocationTimezoneOffset(latitude, longitude)
                setTimeZoneOffset(locationTimeZoneOffsetResult.offset.toDouble())
            }
            _sunUiState.update { currentState ->
                currentState.copy(
                    latitude = latitude,
                    longitude = longitude
                )
            }

            val sunTimes = getSunRiseNoonFall(sunUiState.value.chosenDate.toString()+"T12:00:00.000Z", sunUiState.value.timeZoneOffset, latitude, longitude)
            setSolarTimes(sunTimes[0], sunTimes[1], sunTimes[2])
        }
    }

    //calls fetchLocation method with provider client, then updates latitude and longitude in uiState with return value
    fun getCurrentPosition(fusedLocationProviderClient: FusedLocationProviderClient) {
        viewModelScope.launch() {
            val location = fetchLocation(fusedLocationProviderClient)
            if (location != null) {
                setCoordinates(location.first, location.second, true)
            }
        }
    }

    fun setNewDate(year: Int, month: Int, day: Int){
        _sunUiState.update { currentState ->
            currentState.copy(
                chosenDate = LocalDate.of(year, month, day)
            )
        }

        val sunTimes = getSunRiseNoonFall(sunUiState.value.chosenDate.toString()+"T12:00:00.000Z", sunUiState.value.timeZoneOffset, sunUiState.value.latitude, sunUiState.value.longitude)
        setSolarTimes(sunTimes[0], sunTimes[1], sunTimes[2])
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

    fun loadTimeZoneOffset(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val locationTimeZoneOffsetResult = sunDataSource.fetchLocationTimezoneOffset(latitude, longitude)

                setTimeZoneOffset(locationTimeZoneOffsetResult.offset.toDouble())

            } catch (e: Throwable) {
                Log.d("error", "uh oh" + e.toString())
            }
        }
    }

    fun setTimeZoneOffset(timeZoneOffset: Double) {
        _sunUiState.update { currentState ->
            currentState.copy(
                timeZoneOffset = timeZoneOffset
            )
        }
    }
    fun updateDay(day: Int){

        setNewDate(_sunUiState.value.chosenDate.year, _sunUiState.value.chosenDate.monthValue, day)
    }

    fun updateMonth(month: Int, maxDate: Int){
        var day = _sunUiState.value.chosenDate.dayOfMonth
        if (maxDate < day){
            day = maxDate
        }
        setNewDate(_sunUiState.value.chosenDate.year, month, day)
    }

    fun updateYear(year: Int){
        setNewDate(year, _sunUiState.value.chosenDate.monthValue, _sunUiState.value.chosenDate.dayOfMonth)
    }

}

