package com.example.sunandmoon.viewModel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.calculateSunPosition
import com.example.sunandmoon.data.ARUIState
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.getSunRiseNoonFall
import com.example.sunandmoon.util.fetchLocation
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@HiltViewModel
class ARViewModel  @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : ViewModel() {

    private val dataSource = DataSource()

    private val _arUIState = MutableStateFlow(
        ARUIState(
            sunZenith = null,
            sunAzimuth = null,
            chosenDateTime = LocalDateTime.now(),
            timeZoneOffset = null,
            location = null,
        )
    )

    val arUIState: StateFlow<ARUIState> = _arUIState.asStateFlow()

    init {
        Log.i("ararar","aaa -1")
        getAndSetCurrentPosition()
        //val sunTimes = getSunRiseNoonFall(shootInfoUIState.value.shoot.date, shootInfoUIState.value.shoot.timeZoneOffset, shootInfoUIState.value.shoot.location.latitude, shootInfoUIState.value.shoot.location.longitude)
        //setSolarTimes(sunTimes[0], sunTimes[1], sunTimes[2])
    }

    private fun setSunPosition(location: Location, timeZoneOffset: Double) {
        val sunPosition = calculateSunPosition(_arUIState.value.chosenDateTime, timeZoneOffset, location)
        Log.i("matte", sunPosition.second.toString())
        _arUIState.update { currentState ->
            currentState.copy(
                sunAzimuth = sunPosition.first,
                sunZenith = sunPosition.second
            )
        }
    }

    fun setTimeZoneOffset(timeZoneOffset: Double) {
        _arUIState.update { currentState ->
            currentState.copy(
                timeZoneOffset = timeZoneOffset
            )
        }
    }

    fun setCoordinates(newLocation: Location, setTimeZoneOffset: Boolean) {
        viewModelScope.launch {
            if (setTimeZoneOffset) {
                val locationTimeZoneOffsetResult =
                    dataSource.fetchLocationTimezoneOffset(newLocation)
                setTimeZoneOffset(locationTimeZoneOffsetResult.offset.toDouble())
            }

            _arUIState.update { currentState ->
                currentState.copy(
                    location = newLocation
                )
            }

            val timeZoneOffset = _arUIState.value.timeZoneOffset
            if(timeZoneOffset != null) {
                setSunPosition(newLocation, timeZoneOffset)
            }
        }
    }

    //calls fetchLocation method with provider client, then updates latitude and longitude in uiState with return value
    fun getAndSetCurrentPosition() {
        viewModelScope.launch() {
            fetchLocation(fusedLocationProviderClient) { location: Location, setTimeZoneOffset: Boolean ->
                setCoordinates(
                    location,
                    setTimeZoneOffset
                )
            }
        }
    }

    // the rest is for the AR settings:

    fun openCloseARSettings() {
        _arUIState.update { currentState ->
            currentState.copy(
                editARSettingsIsOpened = !_arUIState.value.editARSettingsIsOpened
            )
        }
    }

    private fun setNewDate(year: Int, month: Int, day: Int) {
        _arUIState.update { currentState ->
            currentState.copy(
                chosenDateTime = LocalDateTime.of(LocalDate.of(year, month, day), _arUIState.value.chosenDateTime.toLocalTime())
            )
        }

        if(_arUIState.value.chosenSunPositionIndex != 0) {
            updateTimeToChosenSunPosition()
        }

        val location = _arUIState.value.location
        val timeZoneOffset = _arUIState.value.timeZoneOffset
        if(location != null && timeZoneOffset != null) {
            setSunPosition(location, timeZoneOffset)
        }
    }

    fun updateDay(day: Int) {

        setNewDate(
            _arUIState.value.chosenDateTime.year,
            _arUIState.value.chosenDateTime.monthValue,
            day
        )
    }

    fun updateMonth(month: Int, maxDay: Int) {
        var day = _arUIState.value.chosenDateTime.dayOfMonth

        if (maxDay < day) {
            day = maxDay
        }
        setNewDate(_arUIState.value.chosenDateTime.year, month, day)
    }

    fun updateYear(year: Int) {
        setNewDate(
            year,
            _arUIState.value.chosenDateTime.monthValue,
            _arUIState.value.chosenDateTime.dayOfMonth
        )
    }

    fun updateTime(time: LocalTime){
        val newDateTime = _arUIState.value.chosenDateTime.withHour(time.hour).withMinute(time.minute)
        viewModelScope.launch {
            _arUIState.update { currentState ->
                currentState.copy(
                    chosenDateTime  = newDateTime
                )
            }

            val location = _arUIState.value.location
            val timeZoneOffset = _arUIState.value.timeZoneOffset
            if(location != null && timeZoneOffset != null) {
                setSunPosition(location, timeZoneOffset)
            }
        }


    }

    fun timePickerSwitch(enabled: Boolean){
        viewModelScope.launch {
            _arUIState.update { currentState ->
                currentState.copy(
                    editTimeEnabled = enabled
                )
            }
        }

    }

    fun updateSunPositionIndex(newIndex: Int){
        viewModelScope.launch {
            _arUIState.update { currentState ->
                currentState.copy(
                    chosenSunPositionIndex = newIndex
                )
            }
            updateTimeToChosenSunPosition()
        }
    }

    private fun updateTimeToChosenSunPosition(){
        val timeZoneOffset = _arUIState.value.timeZoneOffset
        val location = _arUIState.value.location
        if(timeZoneOffset == null || location == null) return

        val sunTimes = getSunRiseNoonFall(
            localDateTime = _arUIState.value.chosenDateTime,
            timeZoneOffset = timeZoneOffset,
            location = location
        )
        when(_arUIState.value.chosenSunPositionIndex){
            0 -> updateTime(LocalTime.now().withSecond(0).withNano(0))
            1 -> updateTime(sunTimes[0])
            2 -> updateTime(sunTimes[1])
            3 -> updateTime(sunTimes[2])
        }
    }
}
