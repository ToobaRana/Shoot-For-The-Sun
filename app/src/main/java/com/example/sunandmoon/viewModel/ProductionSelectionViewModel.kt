package com.example.sunandmoon.viewModel


import android.location.Location
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.ProductionSelectionUIState
import com.example.sunandmoon.data.util.Production
import com.example.sunandmoon.data.util.Shoot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.sunandmoon.data.localDatabase.AppDatabase
import com.example.sunandmoon.data.localDatabase.dao.ProductionDao
import com.example.sunandmoon.data.localDatabase.dao.ShootDao
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableProduction
import com.example.sunandmoon.data.localDatabase.storableShootsToNormalShoots
import com.example.sunandmoon.model.LocationForecastModel.LocationForecast
import com.example.sunandmoon.model.LocationForecastModel.Timeseries
import com.example.sunandmoon.util.getCorrectTimeObject
import com.example.sunandmoon.util.getWeatherIcon
import com.example.sunandmoon.util.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

enum class SelectionPages {
    PRODUCTIONS, SHOOTS, PRODUCTION_SHOOTS
}

@HiltViewModel
class ProductionSelectionViewModel @Inject constructor(
    private val database: AppDatabase
) : ViewModel() {

    private val dataSource = DataSource()

    val productionDao: ProductionDao = database.productionDao()
    val shootDao: ShootDao = database.shootDao()

    private val _productionSelectionUIState = MutableStateFlow(
        ProductionSelectionUIState()
    )

    // to reduce the amount of API-calls needed
    val retrievedWeatherData: MutableMap<Location, LocationForecast> = mutableMapOf()

    val productionSelectionUIState: StateFlow<ProductionSelectionUIState> = _productionSelectionUIState.asStateFlow()

    init {
        // get previously saved productions
        //saveProduction()
        getAllProductions()
        getAllIndependentShoots()

    }


    fun getAllProductions() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val allProductions = productionDao.getAll(_productionSelectionUIState.value.productionOrderBy.value)
                var productionList = mutableListOf<Production>()
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
                    _productionSelectionUIState.update { currentState ->
                        currentState.copy(
                            productionsList = productionList
                        )
                    }
                }
            }
        }
    }

    fun getAllIndependentShoots() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val allIndependentShoots = shootDao.getAllIndependentShoots(_productionSelectionUIState.value.shootOrderBy.value)
                val shootList = storableShootsToNormalShoots(allIndependentShoots)

                _productionSelectionUIState.update { currentState ->
                    currentState.copy(
                        independentShootsList = shootList
                    )
                }

                checkIfWeatherMatchesPreferences(shootList, SelectionPages.SHOOTS)
            }
        }
    }

    fun saveProduction(name: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                productionDao.insert(StorableProduction(
                    uid = 0,
                    name = name,
                    startDateTime = null,
                    endDateTime = null
                ))
                getAllProductions()
            }
        }
    }

    fun deleteProduction() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val idToDelete: Int? = _productionSelectionUIState.value.selectedProduction?.id
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

    fun changeCurrentPageIndex() {
        _productionSelectionUIState.update { currentState ->
            val newPageIndex = (
                    if(_productionSelectionUIState.value.currentPageIndex == 0)
                        SelectionPages.SHOOTS.ordinal
                    else
                        SelectionPages.PRODUCTIONS.ordinal
                )

            currentState.copy(
                currentPageIndex = newPageIndex
            )
        }
    }

    fun getShootsInProduction(production: Production) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val productionShoots = production.id?.let { shootDao.loadByProductionId(it, _productionSelectionUIState.value.shootOrderBy.value) }
                val shootList = storableShootsToNormalShoots(productionShoots)

                _productionSelectionUIState.update { currentState ->
                    currentState.copy(
                        productionShootsList = shootList
                    )
                }

                checkIfWeatherMatchesPreferences(shootList, SelectionPages.PRODUCTION_SHOOTS)
            }
        }
    }

    fun goIntoProduction(production: Production) {
        _productionSelectionUIState.update { currentState ->
            currentState.copy(
                selectedProduction = production,
                currentPageIndex = SelectionPages.PRODUCTION_SHOOTS.ordinal,
            )
        }
        getShootsInProduction(production)
    }

    fun goOutOfProduction() {
        _productionSelectionUIState.update { currentState ->
            currentState.copy(
                currentPageIndex = SelectionPages.PRODUCTIONS.ordinal,
                selectedProduction = null
            )
        }
    }
    fun setProductionName(name: String?){
        _productionSelectionUIState.update { currentState ->
            currentState.copy(
                newProductionName = name
            )

        }
    }

    fun setShowPreferredWeatherDialog(shootToShowPreferredWeatherDialogFor: Shoot?){
        _productionSelectionUIState.update { currentState ->
            currentState.copy(
                shootToShowPreferredWeatherDialogFor = shootToShowPreferredWeatherDialogFor
            )
        }
    }

    fun checkIfWeatherMatchesPreferences(shoots: List<Shoot>, selectionPage: SelectionPages) {
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
                _productionSelectionUIState.update { currentState ->
                    currentState.copy(
                        independentShootsList = updatedShoots
                    )
                }
            }
            else {
                _productionSelectionUIState.update { currentState ->
                    currentState.copy(
                        productionShootsList = updatedShoots
                    )
                }
            }
        }
    }

    fun getWeatherIconOfShoot(shoot: Shoot): Int? {
        retrievedWeatherData.forEach {
            if(shoot.location.distanceTo(it.key) <= 1000) {
                val correctTimeObject = getCorrectTimeObject(shoot.dateTime, it.value)
                var weatherIconCode: String? = correctTimeObject?.data?.next_1_hours?.summary?.symbol_code
                if(weatherIconCode == null) weatherIconCode = correctTimeObject?.data?.next_6_hours?.summary?.symbol_code
                return getWeatherIcon(weatherIconCode)
            }
        }
        return null
    }
}