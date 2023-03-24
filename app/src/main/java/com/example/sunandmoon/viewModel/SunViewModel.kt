package com.example.sunandmoon.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.SunUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SunViewModel : ViewModel() {

    private val sunDataSource = DataSource()
     //sunDataSource.fetchSunrise3Data("sun", 59.933333, 10.716667, "2022-12-18", "+01:00" ).properties.sunrise.time
    private val _sunUiState = MutableStateFlow(
        SunUiState(
            sunRiseTime = "not loaded", sunSetTime = "not loaded", solarNoon = "not loaded", locationSearchResults = listOf()
        )
    )

    val sunUiState: StateFlow<SunUiState> = _sunUiState.asStateFlow()

    init {
        loadSunInformation()
    }

    private fun loadSunInformation(){
        viewModelScope.launch {
            try{
                val sunRiseTime = sunDataSource.fetchSunrise3Data("sun", 59.933333, 10.716667, "2022-12-18", "+01:00" ).properties.sunrise.time

                val sunSetTime = sunDataSource.fetchSunrise3Data("sun", 59.933333, 10.716667, "2022-12-18", "+01:00" ).properties.sunset.time

                val solarNoon = sunDataSource.fetchSunrise3Data("sun", 59.933333, 10.716667, "2022-12-18", "+01:00" ).properties.solarnoon.time

                _sunUiState.value = SunUiState(
                    sunRiseTime = sunRiseTime,
                    sunSetTime = sunSetTime,
                    solarNoon = solarNoon,
                    locationSearchResults = listOf()
                )

                Log.d("test",sunUiState.value.sunRiseTime + sunUiState.value.sunSetTime + sunUiState.value.solarNoon)
            }catch (e: Throwable){

                Log.d("error", "uh oh")
            }
        }
    }

    fun loadLocationSearchResults(query: String) {
        viewModelScope.launch {
            try{
                val locationSearchResults = sunDataSource.fetchLocationSearchResults(query, 10)

                _sunUiState.value = SunUiState(
                    sunRiseTime = _sunUiState.value.sunRiseTime,
                    sunSetTime = _sunUiState.value.sunSetTime,
                    solarNoon = _sunUiState.value.solarNoon,
                    locationSearchResults = locationSearchResults
                )

            }catch (e: Throwable){
                Log.d("error", "uh oh" + e.toString())
            }
        }
    }
}