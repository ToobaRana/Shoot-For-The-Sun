package com.example.sunandmoon.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.CreateShootUIState
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.fetchLocation
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class CreateShootViewModel : ViewModel() {

    private val dataSource = DataSource()

    private val _createShootUIState = MutableStateFlow(
        CreateShootUIState(
            locationSearchQuery = "UiO",
            locationSearchResults = listOf(),
            locationEnabled = false,
            latitude = 59.943965,
            longitude = 10.7178129,
            chosenDate = LocalDateTime.now(),
            timeZoneOffset = 2.0
        )
    )

    val createShootUIState: StateFlow<CreateShootUIState> = _createShootUIState.asStateFlow()

    init {

    }

    fun setLocationSearchQuery(inputQuery: String) {
        _createShootUIState.update { currentState ->
            currentState.copy(
                locationSearchQuery = inputQuery
            )
        }
    }

    fun loadLocationSearchResults(query: String) {
        viewModelScope.launch {
            try {
                val locationSearchResults = dataSource.fetchLocationSearchResults(query, 10)

                _createShootUIState.update { currentState ->
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
        _createShootUIState.update { currentState ->
            currentState.copy(
                locationEnabled = newValue
            )
        }
    }

    fun setCoordinates(latitude: Double, longitude: Double, setTimeZoneOffset: Boolean) {
        viewModelScope.launch {
            if (setTimeZoneOffset) {
                val locationTimeZoneOffsetResult =
                    dataSource.fetchLocationTimezoneOffset(latitude, longitude)
                setTimeZoneOffset(locationTimeZoneOffsetResult.offset.toDouble())
            }
            _createShootUIState.update { currentState ->
                currentState.copy(
                    latitude = latitude,
                    longitude = longitude
                )
            }
        }
    }

    //calls fetchLocation method with provider client, then updates latitude and longitude in uiState with return value
    fun getCurrentPosition(fusedLocationProviderClient: FusedLocationProviderClient) {
        viewModelScope.launch() {
            val location =
                fetchLocation(fusedLocationProviderClient) { latitude: Double, longitude: Double, setTimeZoneOffset: Boolean ->
                    setCoordinates(
                        latitude,
                        longitude,
                        setTimeZoneOffset
                    )
                }


        }
    }

    fun setNewDate(year: Int, month: Int, day: Int) {
        _createShootUIState.update { currentState ->
            currentState.copy(
                chosenDate = LocalDateTime.of(year, month, day, 12, 0, 0)
            )
        }
    }

    fun loadTimeZoneOffset(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val locationTimeZoneOffsetResult =
                    dataSource.fetchLocationTimezoneOffset(latitude, longitude)

                setTimeZoneOffset(locationTimeZoneOffsetResult.offset.toDouble())

            } catch (e: Throwable) {
                Log.d("error", "uh oh" + e.toString())
            }
        }
    }

    fun setTimeZoneOffset(timeZoneOffset: Double) {
        _createShootUIState.update { currentState ->
            currentState.copy(
                timeZoneOffset = timeZoneOffset
            )
        }
    }

    fun updateDay(day: Int) {

        setNewDate(
            _createShootUIState.value.chosenDate.year,
            _createShootUIState.value.chosenDate.monthValue,
            day
        )
    }

    fun updateMonth(month: Int, maxDate: Int) {
        var day = _createShootUIState.value.chosenDate.dayOfMonth
        if (maxDate < day) {
            day = maxDate
        }
        setNewDate(_createShootUIState.value.chosenDate.year, month, day)
    }

    fun updateYear(year: Int) {
        setNewDate(
            year,
            _createShootUIState.value.chosenDate.monthValue,
            _createShootUIState.value.chosenDate.dayOfMonth
        )
    }

    fun updateShootName(inputName: String) {
        _createShootUIState.update { currentState ->
            currentState.copy(
                name = inputName
            )
        }
    }
}

