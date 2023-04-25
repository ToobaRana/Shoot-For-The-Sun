package com.example.sunandmoon.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.ShootInfoUIState
import com.example.sunandmoon.data.util.Shoot
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

    private val dataSource = DataSource()

    private val _shootInfoUIState = MutableStateFlow(
        ShootInfoUIState(
            shoot = Shoot(),
            sunriseTime = "05:23",
            solarNoonTime = "14:06",
            sunsetTime = "21:02"
        )
    )

    val shootInfoUIState: StateFlow<ShootInfoUIState> = _shootInfoUIState.asStateFlow()

    init {
        //val sunTimes = getSunRiseNoonFall(shootInfoUIState.value.shoot.date, shootInfoUIState.value.shoot.timeZoneOffset, shootInfoUIState.value.shoot.location.latitude, shootInfoUIState.value.shoot.location.longitude)
        //setSolarTimes(sunTimes[0], sunTimes[1], sunTimes[2])
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

    fun setShoot(shoot: Shoot) {
        _shootInfoUIState.update { currentState ->
            currentState.copy(
                shoot = shoot
            )
        }
        val sunTimes = getSunRiseNoonFall(shoot.date, shoot.timeZoneOffset, shoot.location.latitude, shoot.location.longitude)
        setSolarTimes(sunTimes[0], sunTimes[1], sunTimes[2])
    }
}
