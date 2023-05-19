package com.example.sunandmoon.viewModel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.CreateShootUIState
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.PreferableWeather
import com.example.sunandmoon.data.fetchLocation
import com.example.sunandmoon.data.localDatabase.AppDatabase
import com.example.sunandmoon.data.localDatabase.dao.ProductionDao
import com.example.sunandmoon.data.localDatabase.dao.ShootDao
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableShoot
import com.example.sunandmoon.getSunRiseNoonFall
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CreateShootViewModel  @Inject constructor(
    private val database: AppDatabase,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : ViewModel() {

    private val dataSource = DataSource()

    val productionDao: ProductionDao = database.productionDao()
    val shootDao: ShootDao = database.shootDao()

    private val _createShootUIState = MutableStateFlow(
        CreateShootUIState(
            locationSearchQuery = "UiO",
            locationSearchResults = listOf(),
            locationEnabled = false,
            latitude = 59.943965,
            longitude = 10.7178129,
            chosenDate = LocalDateTime.now().withSecond(0).withNano(0),
            timeZoneOffset = 2.0,
            timeEnabled = true,
            chosenSunPositionIndex = 0
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
                setTimeZoneOffset(locationTimeZoneOffsetResult.offset)
            }
            _createShootUIState.update { currentState ->
                currentState.copy(
                    latitude = latitude,
                    longitude = longitude
                )
            }
            updateTimeOfChosenSunPosition()
        }
        //setLocationQuery()
    }

    //calls fetchLocation method with provider client, then updates latitude and longitude in uiState with return value
    fun getCurrentPosition() {
        viewModelScope.launch() {

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

                setTimeZoneOffset(locationTimeZoneOffsetResult.offset)

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

    fun updateMonth(month: Int, maxDay: Int) {
        var day = _createShootUIState.value.chosenDate.dayOfMonth

        if (maxDay < day) {
            day = maxDay
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

    fun saveShoot() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val shootId: Int = if(_createShootUIState.value.currentShootBeingEditedId != null) _createShootUIState.value.currentShootBeingEditedId!! else 0

                val storableShoot = StorableShoot(
                    uid = shootId,
                    parentProductionId = _createShootUIState.value.parentProductionId,
                    name = _createShootUIState.value.name,
                    locationName = _createShootUIState.value.locationSearchQuery,
                    latitude = _createShootUIState.value.latitude,
                    longitude = _createShootUIState.value.longitude,
                    dateTime = _createShootUIState.value.chosenDate,
                    timeZoneOffset = _createShootUIState.value.timeZoneOffset,
                    preferredWeather = _createShootUIState.value.preferredWeather
                )

                if(shootId != 0) {
                    shootDao.update(storableShoot)
                }
                else {
                    shootDao.insert(storableShoot)
                }

                // update the interval attributes of the parent production
                val parentProductionId: Int? = _createShootUIState.value.parentProductionId
                if(parentProductionId != null) {
                    productionDao.updateDateInterval(parentProductionId)
                }
            }
        }
    }

    fun setParentProductionId(id: Int) {
        viewModelScope.launch {
            _createShootUIState.update { currentState ->
                currentState.copy(
                    parentProductionId = id
                )
            }
        }
    }

    fun setCurrentShootBeingEditedId(id: Int) {
        viewModelScope.launch {
            _createShootUIState.update { currentState ->
                currentState.copy(
                    currentShootBeingEditedId = id
                )
            }
        }
    }
    fun updateTime(time: LocalTime){
        val newDateTime = _createShootUIState.value.chosenDate.withHour(time.hour).withMinute(time.minute)
        viewModelScope.launch {
            _createShootUIState.update { currentState ->
                currentState.copy(
                    chosenDate  = newDateTime
                )
            }
        }
    }
    //activates and deactivates timepicker
    fun timePickerSwitch(enabled: Boolean){
        viewModelScope.launch {
            _createShootUIState.update { currentState ->
                currentState.copy(
                    timeEnabled = enabled
                )
            }
        }

    }
    private fun setLocationQuery(){
        val newPlace = //reverseApi-call
        viewModelScope.launch {
            _createShootUIState.update { currentState ->
                currentState.copy(
                    //locationSearchQuery = newPlace
                )
            }
        }
    }
    fun updateSunPositionIndex(newIndex: Int){



        viewModelScope.launch {
            _createShootUIState.update { currentState ->
                currentState.copy(
                    chosenSunPositionIndex = newIndex
                )
            }
            updateTimeOfChosenSunPosition()
        }

    }
    private fun updateTimeOfChosenSunPosition(){
        val sunTimes = getSunRiseNoonFall(
            localDateTime = _createShootUIState.value.chosenDate,
            timeZoneOffset = _createShootUIState.value.timeZoneOffset,
            location = Location("").apply {
                latitude = _createShootUIState.value.latitude
                longitude = _createShootUIState.value.longitude
            }
        )
        when(_createShootUIState.value.chosenSunPositionIndex){
            0 -> updateTime(LocalTime.now().withSecond(0).withNano(0))
            1 -> updateTime(LocalTime.parse(sunTimes[0]))
            2 -> updateTime(LocalTime.parse(sunTimes[1]))
            3 -> updateTime(LocalTime.parse(sunTimes[2]))
        }

    }

    fun setUnsetPreferredWeather(preferableWeather: PreferableWeather){
        if(preferableWeather in _createShootUIState.value.preferredWeather) {
            _createShootUIState.update { currentState ->
                currentState.copy(
                    preferredWeather = _createShootUIState.value.preferredWeather.minus(preferableWeather)
                )
            }
        }
        else {
            _createShootUIState.update { currentState ->
                currentState.copy(
                    preferredWeather = _createShootUIState.value.preferredWeather.plus(preferableWeather)
                )
            }
        }
    }
}

