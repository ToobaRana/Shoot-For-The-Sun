package com.example.sunandmoon.viewModel


import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.ShootSelectionUIState
import com.example.sunandmoon.data.dataUtil.Production
import com.example.sunandmoon.data.dataUtil.Shoot
import com.example.sunandmoon.data.localDatabase.AppDatabase
import com.example.sunandmoon.data.localDatabase.dao.ProductionDao
import com.example.sunandmoon.data.localDatabase.dao.ProductionOrderBy
import com.example.sunandmoon.data.localDatabase.dao.ShootDao
import com.example.sunandmoon.data.localDatabase.dao.ShootOrderBy
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableProduction
import com.example.sunandmoon.data.localDatabase.storableShootsToNormalShoots
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
import java.time.LocalDateTime
import javax.inject.Inject

enum class SelectionPages {
    PRODUCTIONS, SHOOTS, PRODUCTION_SHOOTS
}

@HiltViewModel
class ShootSelectionViewModel @Inject constructor(
    database: AppDatabase
) : ViewModel() {

    private val dataSource = DataSource()

    val productionDao: ProductionDao = database.productionDao()
    val shootDao: ShootDao = database.shootDao()

    private val _shootSelectionUIState = MutableStateFlow(
        ShootSelectionUIState()
    )

    // to reduce the amount of API-calls needed
    val retrievedWeatherData: MutableMap<Location, LocationForecast> = mutableMapOf()

    val shootSelectionUIState: StateFlow<ShootSelectionUIState> = _shootSelectionUIState.asStateFlow()

    init {
        // get previously saved productions
        //saveProduction()
        getAllProductions()
        getAllSoloShoots()
    }

    //gets all productions from database
    fun getAllProductions() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val allProductions = productionDao.getAll(_shootSelectionUIState.value.productionOrderBy.value, "%" + _shootSelectionUIState.value.searchQuery + "%")
                val productionList = mutableListOf<Production>()
                allProductions.forEach() { storableProduction ->
                    productionList.add(
                        Production(
                            id = storableProduction.uid,
                            name = storableProduction.name,
                            duration = Pair(
                                storableProduction.startDateTime,
                                storableProduction.endDateTime
                            )
                        )
                    )
                }
                withContext(Dispatchers.Main) {
                    _shootSelectionUIState.update { currentState ->
                        currentState.copy(
                            productionsList = productionList
                        )
                    }
                }
            }
        }
    }

    //gets all soloshoots from api
    fun getAllSoloShoots() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val allSoloShoots = shootDao.getAllSoloShoots(_shootSelectionUIState.value.shootOrderBy.value, "%" + _shootSelectionUIState.value.searchQuery + "%")
                val shootList = storableShootsToNormalShoots(allSoloShoots)

                _shootSelectionUIState.update { currentState ->
                    currentState.copy(
                        soloShootsList = shootList
                    )
                }

                checkIfWeatherMatchesPreferences(shootList, SelectionPages.SHOOTS)
            }
        }
    }

    //saves production with new id assigned by StoreableProduction
    fun saveProduction(name: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val selectedProduction = _shootSelectionUIState.value.selectedProduction
                if(selectedProduction == null) {
                    productionDao.insert(StorableProduction(
                        uid = 0,
                        name = name,
                        startDateTime = null,
                        endDateTime = null
                    ))
                } else {
                    val id = selectedProduction.id
                    if(id != null) {
                        productionDao.update(StorableProduction(
                            uid = id,
                            name = name,
                            startDateTime = selectedProduction.duration.first,
                            endDateTime = selectedProduction.duration.second
                        ))

                        _shootSelectionUIState.update { currentState ->
                            currentState.copy(
                                selectedProduction = selectedProduction.copy(name = name)
                            )
                        }
                    }
                }
                getAllProductions()
            }
        }
    }

    //deletes production with the id in uiState
    fun deleteProduction() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val idToDelete: Int? = _shootSelectionUIState.value.selectedProduction?.id
                Log.i("aaa12345", idToDelete.toString())
                if(idToDelete != null) {
                    shootDao.deleteShootsInProduction(idToDelete)
                    productionDao.delete(productionDao.loadById(idToDelete))
                    getAllProductions()
                }
            }
            goOutOfProduction()
        }
    }

    //changes page index between productions and solo shoots
    fun changeCurrentPageIndex() {
        _shootSelectionUIState.update { currentState ->
            val newPageIndex = (
                    if(_shootSelectionUIState.value.currentPageIndex == 0)
                        SelectionPages.SHOOTS.ordinal
                    else
                        SelectionPages.PRODUCTIONS.ordinal
                )

            currentState.copy(
                currentPageIndex = newPageIndex
            )
        }
    }

    //gets all shoots in production by using id
    fun getShootsInProduction(production: Production) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val productionShoots = production.id?.let { shootDao.loadByProductionId(it, _shootSelectionUIState.value.shootOrderBy.value, "%" + _shootSelectionUIState.value.searchQuery + "%") }
                val shootList = storableShootsToNormalShoots(productionShoots)

                _shootSelectionUIState.update { currentState ->
                    currentState.copy(
                        productionShootsList = shootList
                    )
                }

                checkIfWeatherMatchesPreferences(shootList, SelectionPages.PRODUCTION_SHOOTS)
            }
        }
    }

    //navigates into production
    fun goIntoProduction(production: Production) {
        _shootSelectionUIState.update { currentState ->
            currentState.copy(
                selectedProduction = production,
                currentPageIndex = SelectionPages.PRODUCTION_SHOOTS.ordinal,
            )
        }
        getShootsInProduction(production)
    }

    //exits production
    fun goOutOfProduction() {
        _shootSelectionUIState.update { currentState ->
            currentState.copy(
                currentPageIndex = SelectionPages.PRODUCTIONS.ordinal,
                selectedProduction = null
            )
        }
    }
    //sets production name in uiState
    fun setProductionName(name: String?){
        _shootSelectionUIState.update { currentState ->
            currentState.copy(
                newProductionName = name
            )

        }
    }

    //sets preferred weather dialogue in uistate
    fun setShowPreferredWeatherDialog(shootToShowPreferredWeatherDialogFor: Shoot?){
        _shootSelectionUIState.update { currentState ->
            currentState.copy(
                shootToShowPreferredWeatherDialogFor = shootToShowPreferredWeatherDialogFor
            )
        }
    }

    //checks if weather matches preferences in
    private fun checkIfWeatherMatchesPreferences(shoots: List<Shoot>, selectionPage: SelectionPages) {
        viewModelScope.launch(Dispatchers.IO) {

            val updatedShoots = shoots.toMutableList()

            shoots.forEach { shoot ->

                if(shoot.dateTime < LocalDateTime.now().minusHours(2) || shoot.dateTime > LocalDateTime.now().plusDays(10)) {
                    return@forEach // this is the same as "continue" but for forEach loops
                }

                var weatherData: LocationForecast? = null
                // to reduce the number of API-calls,
                // check if we already have the weather forecast stored for a location within 1km
                retrievedWeatherData.forEach {
                    if(it.key.distanceTo(shoot.location) < 1000) {
                        weatherData = it.value
                    }
                }
                if(weatherData == null) {
                    Log.i("API_PreferredWeather", retrievedWeatherData.size.toString())
                    try {
                        weatherData = dataSource.fetchWeatherAPI(shoot.location.latitude.toString(), shoot.location.longitude.toString())
                    } catch (e: Exception) {
                        return@launch
                    }
                    retrievedWeatherData[shoot.location] = weatherData!!
                }

                val dateTimeObjectForApiUse : LocalDateTime = shoot.dateTime.withMinute(0).withSecond(0).withNano(0)

                val correctTimeObject = getCorrectTimeObject(dateTimeObjectForApiUse, weatherData!!)

                var weatherIconCode : String? = correctTimeObject?.data?.next_1_hours?.summary?.symbol_code
                if(weatherIconCode == null) weatherIconCode = correctTimeObject?.data?.next_6_hours?.summary?.symbol_code

                val weatherIconString = weatherIconCode?.split("_")?.get(0)

                if(weatherIconString != null) {
                    Log.i("weatherPrefrences", weatherIconString)
                    var matchesPreferredWeather: Boolean = shoot.preferredWeather.isEmpty()
                    shoot.preferredWeather.forEach {
                        if(weatherIconString.contains(it.name.lowercase().replace("_", ""))) {
                            matchesPreferredWeather = true
                        }
                    }

                    val indexToReplace = updatedShoots.indexOf(shoot)
                    if (indexToReplace != -1) {
                        updatedShoots[indexToReplace] = shoot.copy(weatherMatchesPreferences = matchesPreferredWeather)
                    }
                }
            }

            if(selectionPage == SelectionPages.SHOOTS) {
                _shootSelectionUIState.update { currentState ->
                    currentState.copy(
                        soloShootsList = updatedShoots
                    )
                }
            }
            else {
                _shootSelectionUIState.update { currentState ->
                    currentState.copy(
                        productionShootsList = updatedShoots
                    )
                }
            }
        }
    }

    //gets weather icons of shoot
    fun getWeatherIconOfShoot(shoot: Shoot): Pair<Int?, String?>? {
        retrievedWeatherData.forEach {
            if(shoot.location.distanceTo(it.key) <= 1000) {
                val correctTimeObject = getCorrectTimeObject(shoot.dateTime, it.value)
                var weatherIconCode: String? = correctTimeObject?.data?.next_1_hours?.summary?.symbol_code
                if(weatherIconCode == null) weatherIconCode = correctTimeObject?.data?.next_6_hours?.summary?.symbol_code
                return Pair(getWeatherIcon(weatherIconCode), weatherIconCode)
            }
        }
        return null
    }

    //opens or closes order by dropdown
    fun openCloseOrderByDropdown() {
        _shootSelectionUIState.update { currentState ->
            currentState.copy(
                orderByDropdownOpened = !_shootSelectionUIState.value.orderByDropdownOpened
            )
        }
    }

    //sets order by for production
    fun setProductionOrderBy(productionOrderBy: ProductionOrderBy) {
        Log.i("productionOrderBy", productionOrderBy.name)
        _shootSelectionUIState.update { currentState ->
            currentState.copy(
                productionOrderBy = productionOrderBy
            )
        }
        getAllProductions()
    }

    //sets order by for shoots
    fun setShootOrderBy(shootOrderBy: ShootOrderBy) {
        _shootSelectionUIState.update { currentState ->
            currentState.copy(
                shootOrderBy = shootOrderBy
            )
        }

        getAllSoloShoots()

        val selectedProduction = _shootSelectionUIState.value.selectedProduction
        if(selectedProduction != null) getShootsInProduction(selectedProduction)
    }

    //sets search query in uiState
    fun setSearchQuery(query: String) {
        _shootSelectionUIState.update { currentState ->
            currentState.copy(
                searchQuery = query
            )
        }

        // this is not very optimized, but we ran out of time
        getAllProductions()
        getAllSoloShoots()
        val selectedProduction = _shootSelectionUIState.value.selectedProduction
        if(selectedProduction != null) getShootsInProduction(selectedProduction)
    }
}