package com.example.sunandmoon.viewModel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.ARUIState
import com.example.sunandmoon.data.calculations.calculateSunPosition
import com.example.sunandmoon.data.calculations.getSunRiseNoonFall
import com.example.sunandmoon.util.fetchLocation
import com.example.sunandmoon.util.getCurrentTimeZoneOffset
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ARViewModel  @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : ViewModel() {

    private val _arUIState = MutableStateFlow(
        ARUIState(
            sunZenith = null,
            sunAzimuth = null,
            chosenDateTime = LocalDateTime.now(),
            timeZoneOffset = getCurrentTimeZoneOffset(),
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

    //sets sun position for uistate
    private fun setSunPosition(location: Location) {
        val sunPosition = calculateSunPosition(_arUIState.value.chosenDateTime, _arUIState.value.timeZoneOffset, location)
        Log.i("matte", sunPosition.second.toString())
        _arUIState.update { currentState ->
            currentState.copy(
                sunAzimuth = sunPosition.first,
                sunZenith = sunPosition.second
            )
        }
    }



    private fun setCoordinates(newLocation: Location?) {
        viewModelScope.launch {

            _arUIState.update { currentState ->
                currentState.copy(
                    location = newLocation
                )
            }

            Log.i("ararar", "new location " + newLocation.toString())

            if (newLocation != null) {
                setSunPosition(newLocation)
            }
        }
    }

    //calls fetchLocation method with provider client, then updates latitude and longitude in uiState with return value
    private fun getAndSetCurrentPosition() {
        viewModelScope.launch {
            fetchLocation(fusedLocationProviderClient) { location: Location? ->
                setCoordinates(
                    location
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

    //sets new date for chosenDateTime
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
        if(location != null) {
            setSunPosition(location)
        }
    }

    //updates date of chosenDateTime
    fun updateDay(day: Int) {

        setNewDate(
            _arUIState.value.chosenDateTime.year,
            _arUIState.value.chosenDateTime.monthValue,
            day
        )
    }

    //updates month of chosenDateTime
    fun updateMonth(month: Int, maxDay: Int) {
        var day = _arUIState.value.chosenDateTime.dayOfMonth

        if (maxDay < day) {
            day = maxDay
        }
        setNewDate(_arUIState.value.chosenDateTime.year, month, day)
    }

    //updates year of chosenDateTime
    fun updateYear(year: Int) {
        setNewDate(
            year,
            _arUIState.value.chosenDateTime.monthValue,
            _arUIState.value.chosenDateTime.dayOfMonth
        )
    }

    //updates time of chosenDateTime
    fun updateTime(time: LocalTime){
        val newDateTime = _arUIState.value.chosenDateTime.withHour(time.hour).withMinute(time.minute)
        viewModelScope.launch {
            _arUIState.update { currentState ->
                currentState.copy(
                    chosenDateTime  = newDateTime
                )
            }

            val location = _arUIState.value.location
            if(location != null) {
                setSunPosition(location)
            }
        }


    }

    //enables and disables timePicker
    fun timePickerSwitch(enabled: Boolean){
        viewModelScope.launch {
            _arUIState.update { currentState ->
                currentState.copy(
                    editTimeEnabled = enabled
                )
            }
        }

    }

    //updatesSunPositionIndex for chosenSunPosition
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

    //updates time for chosen sunPosition
    private fun updateTimeToChosenSunPosition(){
        val timeZoneOffset = _arUIState.value.timeZoneOffset
        val location = _arUIState.value.location ?: return

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

    //sets boolean for shown calibrate magnet
    fun setHasShownCalibrateMagnetMessage(bool: Boolean){
        viewModelScope.launch {
            _arUIState.update { currentState ->
                currentState.copy(
                    hasShownCalibrateMagnetMessage = bool
                )
            }
        }
    }


    //sets boolean for has missing sensors
    fun setMissingSensorsMessage(bool: Boolean){
        viewModelScope.launch {
            _arUIState.update { currentState ->
                currentState.copy(
                    hasShownMissingSensorsMessage = bool
                )
            }
        }
    }
}
