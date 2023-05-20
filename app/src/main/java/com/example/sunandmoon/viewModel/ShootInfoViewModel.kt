package com.example.sunandmoon.viewModel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.ShootInfoUIState
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



@HiltViewModel
class ShootInfoViewModel @Inject constructor(
    private val database: AppDatabase
) : ViewModel() {

    private val dataSource = DataSource()

    val shootDao: ShootDao = database.shootDao()
    val productionDao: ProductionDao = database.productionDao()

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
                val sunTimes = getSunRiseNoonFall(shoot.dateTime, shoot.timeZoneOffset, shoot.location)
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
                if(idToDelete != null) {
                    shootDao.delete(shootDao.loadById(idToDelete))

                    // updates the date interval of the parentProduction if necessary
                    val parentProductionId = _shootInfoUIState.value.shoot?.parentProductionId
                    if(parentProductionId != null) {
                        println("aaaa")
                        productionDao.updateDateInterval(parentProductionId)
                    }
                }
            }
        }
    }

    fun refreshShoot() {
        val idToRefresh: Int? = _shootInfoUIState.value.shoot?.id
        if(idToRefresh != null) {
            getShoot(idToRefresh)
        }
    }
}
