package com.example.sunandmoon.viewModel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.calculateSunPosition
import com.example.sunandmoon.data.ARUIState
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.ShootInfoUIState
import com.example.sunandmoon.data.fetchLocation
import com.example.sunandmoon.data.localDatabase.AppDatabase
import com.example.sunandmoon.data.localDatabase.dao.ProductionDao
import com.example.sunandmoon.data.localDatabase.dao.ShootDao
import com.example.sunandmoon.data.localDatabase.storableShootToNormalShoot
import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.getSunRiseNoonFall
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableShoot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import com.example.sunandmoon.model.LocationForecastModel.LocationForecast
import java.time.LocalDateTime

@HiltViewModel
class ARViewModel  @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : ViewModel() {

    private val dataSource = DataSource()

    private val _arUIState = MutableStateFlow(
        ARUIState(
            sunZenith = null,
            sunAzimuth = null,
            dateTime = LocalDateTime.now(),
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

    fun setSunPosition(location: Location, timeZoneOffset: Double) {
        val sunPosition = calculateSunPosition(_arUIState.value.dateTime, timeZoneOffset, location)
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
        Log.i("ararar","aaa 1.5")
        viewModelScope.launch {
            Log.i("ararar","aaa 1.75")
            if (setTimeZoneOffset) {
                val locationTimeZoneOffsetResult =
                    dataSource.fetchLocationTimezoneOffset(newLocation)
                setTimeZoneOffset(locationTimeZoneOffsetResult.offset.toDouble())
            }

            Log.i("ararar","aaa 1.9")

            _arUIState.update { currentState ->
                currentState.copy(
                    location = newLocation
                )
            }

            val timeZoneOffset = _arUIState.value.timeZoneOffset
            Log.i("ararar","aaa2")
            if(timeZoneOffset != null) {
                Log.i("ararar","aaa3")
                setSunPosition(newLocation, timeZoneOffset)
            }
        }
    }

    //calls fetchLocation method with provider client, then updates latitude and longitude in uiState with return value
    fun getAndSetCurrentPosition() {
        Log.i("ararar","aaa0")
        viewModelScope.launch() {
            Log.i("ararar","aaa 0.5")
            fetchLocation(fusedLocationProviderClient) { location: Location, setTimeZoneOffset: Boolean ->
                Log.i("ararar","aaa1")
                setCoordinates(
                    location,
                    setTimeZoneOffset
                )
            }
        }
    }
}
