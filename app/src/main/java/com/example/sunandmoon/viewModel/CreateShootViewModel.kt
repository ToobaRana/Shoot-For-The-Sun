package com.example.sunandmoon.viewModel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.CreateShootUIState
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.PreferableWeather
import com.example.sunandmoon.data.calculations.getSunRiseNoonFall
import com.example.sunandmoon.data.localDatabase.AppDatabase
import com.example.sunandmoon.data.localDatabase.dao.ProductionDao
import com.example.sunandmoon.data.localDatabase.dao.ShootDao
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableShoot
import com.example.sunandmoon.data.localDatabase.storableShootToNormalShoot
import com.example.sunandmoon.util.fetchLocation
import com.example.sunandmoon.util.findTimeZoneOffsetOfDate
import com.example.sunandmoon.util.getCurrentTimeZoneOffset
import com.example.sunandmoon.util.simplifyLocationNameQuery
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CreateShootViewModel  @Inject constructor(
    database: AppDatabase,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : ViewModel() {

    private val dataSource = DataSource()

    private val productionDao: ProductionDao = database.productionDao()
    private val shootDao: ShootDao = database.shootDao()

    private val _createShootUIState = MutableStateFlow(
        CreateShootUIState(
            locationSearchQuery = "UiO",
            locationSearchResults = listOf(),
            locationEnabled = false,
            location = Location("").apply {
                latitude = 59.943965
                longitude = 10.7178129
            },
            hasGottenCurrentPosition = false,
            chosenDateTime = LocalDateTime.now().withSecond(0).withNano(0),
            timeZoneOffset = getCurrentTimeZoneOffset(),
            editTimeEnabled = true,
            chosenSunPositionIndex = 0,
            timeZoneID = "Europe/Oslo"
        )
    )

    val createShootUIState: StateFlow<CreateShootUIState> = _createShootUIState.asStateFlow()



    //set locationSearchQuery
    fun setLocationSearchQuery(inputQuery: String, format: Boolean) {
        var queryToStore = inputQuery
        if(format) {
            queryToStore = simplifyLocationNameQuery(queryToStore)
        }
        _createShootUIState.update { currentState ->
            currentState.copy(
                locationSearchQuery = queryToStore
            )
        }
    }

    //loads location from search result
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
                Log.d("error", "uh oh$e")
            }
        }
    }

    //updates location
    fun updateLocation(newValue: Boolean) {
        _createShootUIState.update { currentState ->
            currentState.copy(
                locationEnabled = newValue
            )
        }
    }

    //sets coordinates of location
    fun setCoordinates(location: Location) {
        viewModelScope.launch {

            val locationTimeZoneOffsetResult =
                dataSource.fetchLocationTimezoneOffset(location)
            val actualTimeZoneOffset = findTimeZoneOffsetOfDate(locationTimeZoneOffsetResult.timezone_id, _createShootUIState.value.chosenDateTime.toLocalDate().toString())
            setTimeZoneOffset(actualTimeZoneOffset, locationTimeZoneOffsetResult.timezone_id)

            _createShootUIState.update { currentState ->
                currentState.copy(
                    location = location
                )
            }
            if(_createShootUIState.value.chosenSunPositionIndex != 0) {
                updateTimeToChosenSunPosition()
            }
        }
        //setLocationQuery()
    }

    //calls fetchLocation method with provider client, then updates latitude and longitude in uiState with return value
    fun getCurrentPosition() {

        _createShootUIState.update { currentState ->
            currentState.copy(
                hasGottenCurrentPosition = true
            )
        }

        viewModelScope.launch {
            fetchLocation(fusedLocationProviderClient) { location: Location ->
                setCoordinates(
                    location,
                )
                setLocationQuery(location)
            }

        }
    }

    //sets timezoneoffset
    private fun setTimeZoneOffset(timeZoneOffset: Double, timeZoneID: String) {
        _createShootUIState.update { currentState ->
            currentState.copy(
                timeZoneOffset = timeZoneOffset,
                timeZoneID = timeZoneID
            )
        }
    }

    //sets new date for chosenDateTime
    private fun setNewDate(year: Int, month: Int, day: Int) {
        _createShootUIState.update { currentState ->
            currentState.copy(
                chosenDateTime = LocalDateTime.of(LocalDate.of(year, month, day), _createShootUIState.value.chosenDateTime.toLocalTime())
            )
        }

        val actualTimeZoneOffset = findTimeZoneOffsetOfDate(_createShootUIState.value.timeZoneID, _createShootUIState.value.chosenDateTime.toLocalDate().toString())
        setTimeZoneOffset(actualTimeZoneOffset, _createShootUIState.value.timeZoneID)

        if(_createShootUIState.value.chosenSunPositionIndex != 0) {
            updateTimeToChosenSunPosition()
        }
    }

    //updates day for chosenDateTime
    fun updateDay(day: Int) {
        setNewDate(
            _createShootUIState.value.chosenDateTime.year,
            _createShootUIState.value.chosenDateTime.monthValue,
            day
        )
    }
    //updates month for chosenDateTime
    fun updateMonth(month: Int, maxDay: Int) {
        var day = _createShootUIState.value.chosenDateTime.dayOfMonth

        if (maxDay < day) {
            day = maxDay
        }
        setNewDate(_createShootUIState.value.chosenDateTime.year, month, day)
    }
    //updates year for chosenDateTime
    fun updateYear(year: Int) {
        setNewDate(
            year,
            _createShootUIState.value.chosenDateTime.monthValue,
            _createShootUIState.value.chosenDateTime.dayOfMonth
        )
    }
    //updates shoot name
    fun updateShootName(inputName: String) {
        _createShootUIState.update { currentState ->
            currentState.copy(
                name = inputName
            )
        }
    }
    //saves shoot
    fun saveShoot() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val shootId: Int = if(_createShootUIState.value.currentShootBeingEditedId != null) _createShootUIState.value.currentShootBeingEditedId!! else 0

                Log.i("saveShoot", "shootId: $shootId")
                Log.i("saveShoot", "parentProductionId: ${_createShootUIState.value.parentProductionId}")

                val storableShoot = StorableShoot(
                    uid = shootId,
                    parentProductionId = _createShootUIState.value.parentProductionId,
                    name = _createShootUIState.value.name,
                    locationName = _createShootUIState.value.locationSearchQuery,
                    latitude = _createShootUIState.value.location.latitude,
                    longitude = _createShootUIState.value.location.longitude,
                    dateTime = _createShootUIState.value.chosenDateTime,
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
    //sets parent production id for shoot
    fun setParentProductionId(id: Int) {
        viewModelScope.launch {
            _createShootUIState.update { currentState ->
                currentState.copy(
                    parentProductionId = id
                )
            }
        }
    }

    //says which shoot is being edited
    fun setCurrentShootBeingEditedId(id: Int) {
        viewModelScope.launch {

            withContext(Dispatchers.IO) {
                val shoot = storableShootToNormalShoot(shootDao.loadById(id))

                _createShootUIState.update { currentState ->
                    currentState.copy(
                        currentShootBeingEditedId = id,
                        parentProductionId = shoot.parentProductionId,
                        name = shoot.name,
                        locationSearchQuery = shoot.locationName,
                        location = shoot.location,
                        chosenDateTime = shoot.dateTime,
                        preferredWeather = shoot.preferredWeather,
                        timeZoneOffset = shoot.timeZoneOffset
                    )
                }
            }
        }
    }

    //updates time for chosenDateTime
    fun updateTime(time: LocalTime){
        val newDateTime = _createShootUIState.value.chosenDateTime.withHour(time.hour).withMinute(time.minute)
        viewModelScope.launch {
            _createShootUIState.update { currentState ->
                currentState.copy(
                    chosenDateTime  = newDateTime
                )
            }
        }
    }

    //activates and deactivates timepicker
    fun timePickerSwitch(enabled: Boolean){
        viewModelScope.launch {
            _createShootUIState.update { currentState ->
                currentState.copy(
                    editTimeEnabled = enabled
                )
            }
        }

    }

    //sets location query
    private fun setLocationQuery(location: Location){
        viewModelScope.launch {
            val locationName = dataSource.fetchReverseGeocoding(location)
            _createShootUIState.update { currentState ->
                currentState.copy(
                    locationSearchQuery = simplifyLocationNameQuery(locationName)
                )
            }
        }
    }

    //updates index for chosenSunPosition in create shoot
    fun updateSunPositionIndex(newIndex: Int){
        viewModelScope.launch {
            _createShootUIState.update { currentState ->
                currentState.copy(
                    chosenSunPositionIndex = newIndex
                )
            }
            updateTimeToChosenSunPosition()
        }
    }

    //updates time according to chosen index
    private fun updateTimeToChosenSunPosition(){
        val sunTimes = getSunRiseNoonFall(
            localDateTime = _createShootUIState.value.chosenDateTime,
            timeZoneOffset = _createShootUIState.value.timeZoneOffset,
            location = _createShootUIState.value.location
        )
        when(_createShootUIState.value.chosenSunPositionIndex){
            0 -> updateTime(LocalTime.now().withSecond(0).withNano(0))
            1 -> updateTime(sunTimes[0])
            2 -> updateTime(sunTimes[1])
            3 -> updateTime(sunTimes[2])
        }

    }

    //sets or unsets preferred weather
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

