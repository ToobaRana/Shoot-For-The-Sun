package com.example.sunandmoon.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.ShootInfoUIState
import com.example.sunandmoon.data.localDatabase.AppDatabase
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import com.example.sunandmoon.model.LocationForecastModel.LocationForecast



@HiltViewModel
class ShootInfoViewModel @Inject constructor(
    private val database: AppDatabase
) : ViewModel() {

    private val dataSource = DataSource()

    val shootDao: ShootDao = database.shootDao()

    private val _shootInfoUIState = MutableStateFlow(
        ShootInfoUIState(
            sunriseTime = "05:23",
            solarNoonTime = "14:06",
            sunsetTime = "21:02",
            weatherData = null

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

    fun getShoot(shootId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val shoot = storableShootToNormalShoot(shootDao.loadById(shootId))

                _shootInfoUIState.update { currentState ->
                    currentState.copy(
                        shoot = shoot
                    )
                }
                val sunTimes = getSunRiseNoonFall(shoot.date, shoot.timeZoneOffset, shoot.location.latitude, shoot.location.longitude)
                setSolarTimes(sunTimes[0], sunTimes[1], sunTimes[2])

                loadLocationForecast()
            }
        }

    }


    fun loadLocationForecast(){
        viewModelScope.launch(Dispatchers.IO) {
            val weatherData = dataSource.fetchWeatherAPI(shootInfoUIState.value.shoot?.location?.latitude.toString(), shootInfoUIState.value.shoot?.location?.longitude.toString())
            _shootInfoUIState.update { currenState ->
                currenState.copy(
                    weatherData = weatherData
                )
            }

        }
    }

    fun deleteShoot() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val idToDelete: Int? = _shootInfoUIState.value.shoot?.id
                Log.i("aaa12345", idToDelete.toString())
                if(idToDelete != null) {
                    shootDao.delete(shootDao.loadById(idToDelete))
                }
            }
        }
    }
}
