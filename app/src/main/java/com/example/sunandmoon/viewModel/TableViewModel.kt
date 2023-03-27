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

class TableViewModel : ViewModel() {

    private val dataSource = DataSource()
    private val _tableUiState = MutableStateFlow(
        SunUiState(
            sunRiseTime = "not loaded",
            sunSetTime = "not leaded",
            solarNoon = "not leaded",
            locationEnabled = false,
            locationSearchResults = listOf(),
            latitude = 0.0,
            longitude = 0.0
        )
    )


    val tableUiState: StateFlow<SunUiState> = _tableUiState.asStateFlow()

    var date = ""


    init {
        //loadSunInformation(date)
        //loadSunInformation2(date, dataSource)
    }

    fun loadSunInformation(date : String ){
        viewModelScope.launch {
            try{
                val sunRiseTime = dataSource.fetchSunrise3Data("sun", 59.933333, 10.716667, date, "+01:00" ).properties.sunrise.time

                val sunSetTime = dataSource.fetchSunrise3Data("sun", 59.933333, 10.716667, date, "+01:00" ).properties.sunset.time

                val solarNoon = dataSource.fetchSunrise3Data("sun", 59.933333, 10.716667, date, "+01:00" ).properties.solarnoon.time

                _tableUiState.value = SunUiState(
                    sunRiseTime = sunRiseTime,
                    sunSetTime = sunSetTime,
                    solarNoon = solarNoon,
                    locationEnabled = false,
                    locationSearchResults = listOf(),
                    latitude = 0.0,
                    longitude = 0.0
                )

                Log.d("test",tableUiState.value.sunRiseTime + tableUiState.value.sunSetTime + tableUiState.value.solarNoon)
            }catch (e: Throwable){

                Log.d("error", "uh oh")
            }
        }
    }

/*
val date = LocalDate.now().withDayOfMonth(i)
                    val dateString = date.format(formatter)
 */



}