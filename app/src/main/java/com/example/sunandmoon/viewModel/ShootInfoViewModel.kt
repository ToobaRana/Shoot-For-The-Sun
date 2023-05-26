package com.example.sunandmoon.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.ShootInfoUIState
import com.example.sunandmoon.data.calculations.getSunRiseNoonFall
import com.example.sunandmoon.data.localDatabase.AppDatabase
import com.example.sunandmoon.data.localDatabase.dao.ProductionDao
import com.example.sunandmoon.data.localDatabase.dao.ShootDao
import com.example.sunandmoon.data.localDatabase.storableShootToNormalShoot
import com.example.sunandmoon.model.locationForecastModel.LocationForecast
import com.example.sunandmoon.util.getCorrectTimeObject
import com.example.sunandmoon.util.getWeatherIcon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class ShootInfoViewModel @Inject constructor(
    database: AppDatabase
) : ViewModel() {

    private val dataSource = DataSource()

    val shootDao: ShootDao = database.shootDao()
    val productionDao: ProductionDao = database.productionDao()

    private val _shootInfoUIState = MutableStateFlow(
        ShootInfoUIState(
            sunriseTime = "05:23",
            solarNoonTime = "14:06",
            sunsetTime = "21:02",
        )
    )

    val shootInfoUIState: StateFlow<ShootInfoUIState> = _shootInfoUIState.asStateFlow()


    //sets solar times for uiState
    private fun setSolarTimes(sunriseTime: String, solarNoonTime: String, sunsetTime: String) {
        _shootInfoUIState.update { currentState ->
            currentState.copy(
                sunriseTime = sunriseTime,
                solarNoonTime = solarNoonTime,
                sunsetTime = sunsetTime
            )
        }
    }

    //fetches shoot from database by id
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
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                setSolarTimes(sunTimes[0].format(formatter), sunTimes[1].format(formatter), sunTimes[2].format(formatter))

                loadLocationForecast()
            }
        }

    }

    //loads location forecast if there is a network connection
    fun loadLocationForecast(){
        viewModelScope.launch(Dispatchers.IO) {
            var weatherData: LocationForecast? = null
            try {
                weatherData = dataSource.fetchWeatherAPI(shootInfoUIState.value.shoot?.location?.latitude.toString(), shootInfoUIState.value.shoot?.location?.longitude.toString())
            } catch (_: Exception) {
                _shootInfoUIState.update { currentState ->
                    currentState.copy(
                        missingNetworkConnection = true
                    )
                }
            }

            if (weatherData != null) {
                retrieveUsefulWeatherData(weatherData)
            }
        }
    }

    //retrieves weather data needed for shootInfo
    private fun retrieveUsefulWeatherData(weatherData: LocationForecast) {
        val dateAndTime = _shootInfoUIState.value.shoot!!.dateTime

        val correctTimeObject = getCorrectTimeObject(dateAndTime, weatherData)

        var weatherIconCode: String? = correctTimeObject?.data?.next_1_hours?.summary?.symbol_code
        if(weatherIconCode == null) weatherIconCode = correctTimeObject?.data?.next_6_hours?.summary?.symbol_code
        Log.d("symbolCode: ", weatherIconCode.toString())
        val temperature: Double? = correctTimeObject?.data?.instant?.details?.air_temperature
        var rainfallInMm: Double? = correctTimeObject?.data?.next_1_hours?.details?.precipitation_amount
        if(rainfallInMm == null) rainfallInMm = correctTimeObject?.data?.next_6_hours?.details?.precipitation_amount

        val windSpeed: Double? = correctTimeObject?.data?.instant?.details?.wind_speed
        val windDirection: Double? = correctTimeObject?.data?.instant?.details?.wind_from_direction
        val uvIndex: Double? = correctTimeObject?.data?.instant?.details?.ultraviolet_index_clear_sky

        _shootInfoUIState.update { currentState ->
            currentState.copy(
                weatherIcon = getWeatherIcon(weatherIconCode),
                temperature = temperature,
                rainfallInMm = rainfallInMm,
                windSpeed = windSpeed,
                windDirection = windDirection,
                uvIndex = uvIndex
            )
        }
    }

    //deletes shoot with id defined in uiState
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

    //refreshes shoot by id in uiState
    fun refreshShoot() {
        val idToRefresh: Int? = _shootInfoUIState.value.shoot?.id
        if(idToRefresh != null) {
            getShoot(idToRefresh)
        }
    }


}