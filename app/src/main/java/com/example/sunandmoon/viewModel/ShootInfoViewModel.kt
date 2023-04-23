package com.example.sunandmoon.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.ShootInfoUIState
import com.example.sunandmoon.fetchLocation
import com.example.sunandmoon.getSunRiseNoonFall
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ShootInfoViewModel : ViewModel() {

    private val sunDataSource = DataSource()

    //sunDataSource.fetchSunrise3Data("sun", 59.933333, 10.716667, "2022-12-18", "+01:00" ).properties.sunrise.time
    private val _shootInfoUIState = MutableStateFlow(
        ShootInfoUIState(
            sunriseTime = "not calculated",
            solarNoonTime = "not calculated",
            sunsetTime = "not calculated",
            locationSearchQuery = "UiO",
            locationSearchResults = listOf(),
            locationEnabled = true,
            latitude = 59.943965,
            longitude = 10.7178129,
            chosenDate = LocalDateTime.now(),
            timeZoneOffset = 2.0
        )
    )

    val shootInfoUIState: StateFlow<ShootInfoUIState> = _shootInfoUIState.asStateFlow()

    init {
        //loadSunInformation()
        //setCoordinates(item.lat.toDouble(), item.lon.toDouble())
        //Instant.now().toString()
        val sunTimes = getSunRiseNoonFall(shootInfoUIState.value.chosenDate, shootInfoUIState.value.timeZoneOffset, shootInfoUIState.value.latitude, shootInfoUIState.value.longitude)
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

                _shootInfoUIState.update { currentState ->
                    currentState.copy(
                        sunriseTime = sunRiseTime,
                        sunsetTime = sunSetTime,
                        solarNoonTime = solarNoon,
                        locationSearchResults = listOf(),
                    )
                }

                Log.d(
                    "test",
                    shootInfoUIState.value.sunriseTime + shootInfoUIState.value.sunsetTime + shootInfoUIState.value.solarNoonTime
                )
            } catch (e: Throwable) {
                Log.d("error", "uh oh" + e.toString())
            }
        }
    }

    fun setLocationSearchQuery(inputQuery: String) {
        _shootInfoUIState.update { currentState ->
            currentState.copy(
                locationSearchQuery = inputQuery
            )
        }
    }

    fun loadLocationSearchResults(query: String) {
        viewModelScope.launch {
            try {
                val locationSearchResults = sunDataSource.fetchLocationSearchResults(query, 10)

                _shootInfoUIState.update { currentState ->
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
        _shootInfoUIState.update { currentState ->
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
            _shootInfoUIState.update { currentState ->
                currentState.copy(
                    latitude = latitude,
                    longitude = longitude
                )
            }

            val sunTimes = getSunRiseNoonFall(shootInfoUIState.value.chosenDate, shootInfoUIState.value.timeZoneOffset, latitude, longitude)
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
        _shootInfoUIState.update { currentState ->
            currentState.copy(
                chosenDate = LocalDateTime.of(year, month, day, 12, 0, 0)
            )
        }

        val sunTimes = getSunRiseNoonFall(shootInfoUIState.value.chosenDate, shootInfoUIState.value.timeZoneOffset, shootInfoUIState.value.latitude, shootInfoUIState.value.longitude)
        setSolarTimes(sunTimes[0], sunTimes[1], sunTimes[2])
    }


    fun setSolarTimes(sunriseTime: String, solarNoonTime: String, sunsetTime: String) {
        _shootInfoUIState.update { currentState ->
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
        _shootInfoUIState.update { currentState ->
            currentState.copy(
                timeZoneOffset = timeZoneOffset
            )
        }
    }
    fun updateDay(day: Int){

        setNewDate(_shootInfoUIState.value.chosenDate.year, _shootInfoUIState.value.chosenDate.monthValue, day)
    }

    fun updateMonth(month: Int, maxDate: Int){
        var day = _shootInfoUIState.value.chosenDate.dayOfMonth
        if (maxDate < day){
            day = maxDate
        }
        setNewDate(_shootInfoUIState.value.chosenDate.year, month, day)
    }

    fun updateYear(year: Int){
        setNewDate(year, _shootInfoUIState.value.chosenDate.monthValue, _shootInfoUIState.value.chosenDate.dayOfMonth)
    }

}

