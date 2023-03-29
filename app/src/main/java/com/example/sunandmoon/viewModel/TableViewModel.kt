package com.example.sunandmoon.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.SunUiState
import com.example.sunandmoon.data.TableUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class TableViewModel : ViewModel() {

    private val dataSource = DataSource()
    private var sunTimeList = mutableListOf<String>()


    private val _sunUiState = MutableStateFlow(
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
    private val _tableUiState = MutableStateFlow(

        TableUIState(
            dateTableList = mutableListOf(), chosenSunType = "not loaded"
        )
    )

    val tableUiState: StateFlow<TableUIState> = _tableUiState.asStateFlow()
    val sunUiState: StateFlow<SunUiState> = _sunUiState.asStateFlow()

    init {
        loadDateTableList(sunType = "Sunrise")
    }

    fun loadSunInformation(date : String, sunType: String){
        viewModelScope.launch {
            try{

                var sunRiseTime = "not loaded"
                var sunSetTime = "not loaded"
                var solarNoon = "not loaded"

                if (sunType == "Sunrise"){
                    sunRiseTime = dataSource.fetchSunrise3Data("sun", 59.933333, 10.716667, date, "+01:00" ).properties.sunrise.time
                    sunTimeList.add(sunRiseTime)
                }

                if (sunType == "Sunset"){
                    sunSetTime = dataSource.fetchSunrise3Data("sun", 59.933333, 10.716667, date, "+01:00" ).properties.sunset.time
                    sunTimeList.add(sunSetTime)
                }

                if (sunType == "SolarNoon"){
                    solarNoon = dataSource.fetchSunrise3Data("sun", 59.933333, 10.716667, date, "+01:00" ).properties.solarnoon.time
                    sunTimeList.add(solarNoon)
                }

                _sunUiState.value = SunUiState(
                    sunRiseTime = sunRiseTime,
                    sunSetTime = sunSetTime,
                    solarNoon = solarNoon,
                    locationEnabled = false,
                    locationSearchResults = listOf(),
                    latitude = 0.0,
                    longitude = 0.0
                )

                _tableUiState.value = TableUIState(sunTimeList,tableUiState.value.chosenSunType)

                Log.d("size: sunList", sunTimeList.size.toString())
            }catch (e: Throwable){

                Log.d("error", "uh oh")
            }
        }
    }


        fun loadDateTableList(date : String = "2022-12-18", sunType: String){

        viewModelScope.launch {
            try {
                Log.d("sunType", sunType.toString())
                val sameDays = getSameDaysInYear(date)


                Log.d("size: sameDays", sameDays.size.toString())
                sunTimeList.clear()
                for (date in sameDays.sorted()){


                        loadSunInformation(date.toString(), sunType)



                }

            }catch (e : Throwable)
            {
                Log.d("error", "Couldnt load date table list")
            }
        }
    }

    fun getSameDaysInYear(dateString: String): List<LocalDate> {

            val date = LocalDate.parse(dateString)
            val year = date.year
            val sameDays = mutableListOf<LocalDate>()

            for (month in 1..12) {
                if (month < date.month.value){
                    val nextYear = year+1

                    val sameDayOfMonth = LocalDate.of(nextYear, month, date.dayOfMonth)
                    sameDays.add(sameDayOfMonth)

                }
                else{

                    val sameDayOfMonth = LocalDate.of(year, month, date.dayOfMonth)
                    sameDays.add(sameDayOfMonth)

                }

            }
            //println(sameDays.sorted())

            return sameDays.sorted()
        }



}