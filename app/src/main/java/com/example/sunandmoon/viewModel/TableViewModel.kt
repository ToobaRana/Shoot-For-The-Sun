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


    private val _sunUiState = MutableStateFlow(
        SunUiState(
            sunRiseTime = "not loaded", sunSetTime = "not leaded", solarNoon = "not leaded"
        )
    )
    var sunTimeList = mutableListOf<String>()


    private val _tableUiState = MutableStateFlow(
        TableUIState(
            dateTableList = mutableListOf()
        )
    )

    val sunUiState: StateFlow<SunUiState> = _sunUiState.asStateFlow()
    val tableUiState: StateFlow<TableUIState> = _tableUiState.asStateFlow()



    //var date = ""


    init {
        //loadSunInformation(date)
        //loadSunInformation2(date, dataSource)
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
                    solarNoon = solarNoon
                )

                _tableUiState.value = TableUIState(sunTimeList)

                //Log.d("sunListe", _tableUiState.value.dateTableList.toString())

                //Log.d("test",sunUiState.value.sunRiseTime + sunUiState.value.sunSetTime + sunUiState.value.solarNoon)
            }catch (e: Throwable){

                Log.d("error", "uh oh")
            }
        }
    }



        fun loadDateTableList(date : String = LocalDate.now().toString(), sunType: String){
        viewModelScope.launch {
            try {
                val sameDays = getSameDaysInYear(date)


                for (element in sameDays.sorted()){
                    println(element)
                    loadSunInformation(element.toString(), sunType)

                }

                //Log.d("test", sameDays.toString())



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

            //print(date.month.value)
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
            println(sameDays.sorted())

            return sameDays.sorted()
        }











/*
val date = LocalDate.now().withDayOfMonth(i)
                    val dateString = date.format(formatter)
 */



}