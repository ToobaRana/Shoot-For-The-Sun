package com.example.sunandmoon.viewModel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.ShootInfoUIState
import com.example.sunandmoon.data.localDatabase.AppDatabase
import com.example.sunandmoon.data.localDatabase.dao.ShootDao
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableShoot
import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.getSunRiseNoonFall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

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

    fun deleteShoot() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val idToDelete: Int? = _shootInfoUIState.value.shoot?.id
                if(idToDelete != null) {
                    shootDao.delete(shootDao.loadById(idToDelete))
                }
            }
        }
    }

    fun refreshShoot() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val idToRefresh: Int? = _shootInfoUIState.value.shoot?.id

                var storableShoot: StorableShoot? = null
                if(idToRefresh != null) {
                    storableShoot = shootDao.loadById(idToRefresh)
                }
                if(storableShoot == null) return@withContext

                val refreshedShoot: Shoot = Shoot(
                    id = storableShoot.uid,
                    name = storableShoot.name,
                    locationName = storableShoot.locationName,
                    location = Location("").apply {
                        latitude = storableShoot.latitude
                        longitude = storableShoot.longitude
                    },
                    date = storableShoot.date,
                    timeZoneOffset = storableShoot.timeZoneOffset

                )



                _shootInfoUIState.update { currentState ->
                    currentState.copy(
                        shoot = refreshedShoot
                    )
                }
            }
        }
    }
}
