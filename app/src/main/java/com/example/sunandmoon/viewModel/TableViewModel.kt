package com.example.sunandmoon.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.TableUIState
import com.example.sunandmoon.getSunRiseNoonFall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Math.min
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TableViewModel : ViewModel() {

    private val dataSource = DataSource()

    private val _tableUiState = MutableStateFlow(

        TableUIState(
            apiDateTableList = listOf(),
            calculationsDateTableList = listOf("", "", "", "", "", "", "", "", "", "", "", ""),
            latitude = 17.607788,
            longitude = 8.081666,
            chosenDate = LocalDate.now(),
            chosenSunType = "Sunrise",
            timeZoneOffset = 1.0
        )
    )


    val tableUiState: StateFlow<TableUIState> = _tableUiState.asStateFlow()


    init {
        loadSunInformation()
    }

    fun loadSunInformation(){
        viewModelScope.launch {

            var sunRiseTime = "not loaded"
            var sunSetTime = "not loaded"
            var solarNoon = "not loaded"

            var apiDateTableList = mutableListOf<String>()
            var calculationsDateTableList = mutableListOf<String>()

            val sameDaysList = getSameDaysInYear(tableUiState.value.chosenDate)
            val sameDaysListFromJanuary = getSameDaysInYearFromJanuary(tableUiState.value.chosenDate)
            println(sameDaysList)
            println(sameDaysListFromJanuary)

            for (date in sameDaysListFromJanuary.sorted()){
                var stringDate = date.toString() + " 00:00"
                var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                var dateTime = LocalDateTime.parse(stringDate, formatter)

                var calculationSunTime = getSunRiseNoonFall(dateTime, tableUiState.value.timeZoneOffset, tableUiState.value.latitude, tableUiState.value.longitude)
                println(calculationSunTime)
                println(dateTime)
                if (tableUiState.value.chosenSunType == "Sunrise"){
                    calculationsDateTableList.add(calculationSunTime[0])

                }

                if (tableUiState.value.chosenSunType == "SolarNoon"){
                    calculationsDateTableList.add(calculationSunTime[1])
                }

                if (tableUiState.value.chosenSunType == "Sunset"){
                    calculationsDateTableList.add(calculationSunTime[2])
                }

            }

            for (date in sameDaysList.sorted()){

                println(date)
                println("This is the date" + date)

                if (tableUiState.value.chosenSunType == "Sunrise"){
                    sunRiseTime = dataSource.fetchSunrise3Data("sun", tableUiState.value.latitude, tableUiState.value.longitude, date.toString(), "+01:00" ).properties.sunrise.time
                    apiDateTableList.add(sunRiseTime)


                }

                if (tableUiState.value.chosenSunType == "SolarNoon"){
                    solarNoon = dataSource.fetchSunrise3Data("sun", tableUiState.value.latitude, tableUiState.value.longitude, date.toString(), "+01:00" ).properties.solarnoon.time
                    apiDateTableList.add(solarNoon)

                }

                if (tableUiState.value.chosenSunType == "Sunset"){
                    sunSetTime = dataSource.fetchSunrise3Data("sun", tableUiState.value.latitude, tableUiState.value.longitude, date.toString(), "+01:00" ).properties.sunset.time
                    apiDateTableList.add(sunSetTime)

                }
            }


            _tableUiState.update{currentState ->
                currentState.copy(
                    apiDateTableList = apiDateTableList,
                    calculationsDateTableList = calculationsDateTableList

                )
            }
        }
    }

    fun getSameDaysInYear(date: LocalDate): List<LocalDate> {

        val daysInMonth = listOf(31, 28, 31, 30, 31, 30, 31, 31 ,30, 31, 30, 31)

        val year = date.year
        val sameDays = mutableListOf<LocalDate>()

        for (month in 1..12) {
            val daysInThisMonth = daysInMonth[month-1]
            val dayToUse = min(daysInThisMonth, date.dayOfMonth)
            if (month < date.month.value){
                val nextYear = year+1



                val sameDayOfMonth = LocalDate.of(nextYear, month, dayToUse)
                sameDays.add(sameDayOfMonth)

            }
            else{

                val sameDayOfMonth = LocalDate.of(year, month, dayToUse)
                sameDays.add(sameDayOfMonth)

            }

        }
        //println(sameDays.sorted())

        return sameDays.sorted()
    }
    fun getSameDaysInYearFromJanuary(date: LocalDate): List<LocalDate> {

        val daysInMonth = listOf(31, 28, 31, 30, 31, 30, 31, 31 ,30, 31, 30, 31)

        val year = date.year
        val sameDays = mutableListOf<LocalDate>()

        for (month in 1..12) {
            val daysInThisMonth = daysInMonth[month-1]
            val dayToUse = min(daysInThisMonth, date.dayOfMonth)



                val sameDayOfMonth = LocalDate.of(year, month, dayToUse)
                sameDays.add(sameDayOfMonth)



        }
        //println(sameDays.sorted())

        return sameDays.sorted()
    }

    fun setSunType(sunType: String){
        _tableUiState.update{ currentState ->
            currentState.copy(
                chosenSunType = sunType,
            )
        }
    }

}