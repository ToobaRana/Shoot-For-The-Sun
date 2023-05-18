package com.example.sunandmoon.viewModel

import android.location.Location
import android.util.Log
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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class TableViewModel : ViewModel() {

    private val dataSource = DataSource()

    private val _tableUIState = MutableStateFlow(
        TableUIState(
            apiDateTableList = listOf(),
            calculationsDateTableList = listOf("", "", "", "", "", "", "", "", "", "", "", ""),
            locationSearchQuery = "UiO",
            locationSearchResults = listOf(),
            location = Location("").apply {
                latitude = 59.943965
                longitude = 10.7178129
            },
            chosenDate = LocalDateTime.now(),
            chosenSunType = "Sunrise",
            timeZoneOffset = 2.0,
            timezone_id = "Europa/Oslo"
        )
    )

    val tableUIState: StateFlow<TableUIState> = _tableUIState.asStateFlow()

    init {
        loadSunInformation()
    }

    fun loadSunInformation(){
        viewModelScope.launch {

            var sunRiseTime: String
            var sunSetTime: String
            var solarNoon: String

            val apiDateTableList = mutableListOf<String>()
            val calculationsDateTableList = mutableListOf<String>()

            val sameDaysList = getSameDaysInYear(tableUIState.value.chosenDate.toLocalDate())
            val sameDaysListFromJanuary = getSameDaysInYearFromJanuary(tableUIState.value.chosenDate.toLocalDate())


            for (date in sameDaysListFromJanuary.sorted()){
                val stringDate = "$date 00:00"
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                val dateTime = LocalDateTime.parse(stringDate, formatter)

                val calculationSunTime = getSunRiseNoonFall(dateTime, tableUIState.value.timeZoneOffset, tableUIState.value.location)

                if (tableUIState.value.chosenSunType == "Sunrise"){
                    calculationsDateTableList.add(calculationSunTime[0])
                }

                if (tableUIState.value.chosenSunType == "SolarNoon"){
                    calculationsDateTableList.add(calculationSunTime[1])
                }

                if (tableUIState.value.chosenSunType == "Sunset"){
                    calculationsDateTableList.add(calculationSunTime[2])
                }

            }

            for (date in sameDaysList.sorted()){
                val offsetToFindSign = findOffset(tableUIState.value.timezone_id, date.toString())
                val offsetUIState = tableUIState.value.timeZoneOffset
                var offsetString: String

                Log.d("offsetToFindSign", offsetToFindSign.toString())
                Log.d("offsetUIState", offsetUIState.toString())

                val offsetList = offsetUIState.toString().split(".")

                val hours = offsetList[0]
                val hoursWithOutSign = hours.split("")[2]

                val minutesDecimal = offsetList[1]

                var convertToMinutes = convertToMinutesFunction(minutesDecimal)

                println(convertToMinutes)


                if (offsetUIState in -9.9..9.9) {

                    //if it is hours and minutes
                    if (convertToMinutes == "0") {

                        //if negative and decimal
                        offsetString = if(offsetUIState.toString().split("")[1] == "-") {
                            "-0${hoursWithOutSign}:00"
                        }

                        //not negative
                        else{
                            "+0${hours}:00"
                        }
                    }

                    //not minutes, only hours
                    else{
                          //if negative
                        offsetString = if(offsetUIState.toString().split("")[1] == "-") {

                            "-0${hoursWithOutSign}:${convertToMinutes}"
                        }
                        //not negative
                        else{
                            "+0${hours}:${convertToMinutes}"

                        }
                    }

                   //same as above, but checks for values over 10 og under -10
                } else if (offsetUIState in 10.0..25.0 || offsetUIState in -25.0..-10.0) {

                          if (convertToMinutes.toString() == "0") {

                              offsetString = if (offsetUIState.toString().split("")[1] == "-") {
                                  "-${hoursWithOutSign}:00"
                              }

                              else {
                                  "+${hours}:00"
                              }

                          }

                          else{
                              offsetString = if(offsetUIState.toString().split("")[1] == "-") {
                                  "-${hoursWithOutSign}:${convertToMinutes}"
                              }
                              else{
                                  "+${hours}:${convertToMinutes}"
                              }
                          }

                      }

                else {
                    val offsetFromUiState = tableUIState.value.timeZoneOffset.toString().split(".")[0]
                    offsetString = "+0$offsetFromUiState:00"
                    }

                if (tableUIState.value.chosenSunType == "Sunrise"){
                    sunRiseTime = dataSource.fetchSunrise3Data("sun", tableUIState.value.location.latitude, tableUIState.value.location.longitude, date.toString(), offsetString).properties.sunrise.time
                    apiDateTableList.add(sunRiseTime)
                }

                if (tableUIState.value.chosenSunType == "SolarNoon"){
                    solarNoon = dataSource.fetchSunrise3Data("sun", tableUIState.value.location.latitude, tableUIState.value.location.longitude, date.toString(), offsetString).properties.solarnoon.time
                    apiDateTableList.add(solarNoon)
                }

                if (tableUIState.value.chosenSunType == "Sunset"){
                    sunSetTime = dataSource.fetchSunrise3Data("sun", tableUIState.value.location.latitude, tableUIState.value.location.longitude, date.toString(), offsetString).properties.sunset.time
                    apiDateTableList.add(sunSetTime)
                }

            }


            _tableUIState.update{currentState ->
                currentState.copy(
                    apiDateTableList = apiDateTableList,
                    calculationsDateTableList = calculationsDateTableList

                )
            }
        }
    }

    private fun getSameDaysInYear(date: LocalDate): List<LocalDate> {

        val daysInMonth = listOf(31, 28, 31, 30, 31, 30, 31, 31 ,30, 31, 30, 31)

        val year = date.year
        val sameDays = mutableListOf<LocalDate>()

        for (month in 1..12) {
            val daysInThisMonth = daysInMonth[month-1]
            val dayToUse = daysInThisMonth.coerceAtMost(date.dayOfMonth)
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

        return sameDays.sorted()
    }
    private fun getSameDaysInYearFromJanuary(date: LocalDate): List<LocalDate> {

        val daysInMonth = listOf(31, 28, 31, 30, 31, 30, 31, 31 ,30, 31, 30, 31)

        val year = date.year
        val sameDays = mutableListOf<LocalDate>()

        for (month in 1..12) {
            val daysInThisMonth = daysInMonth[month-1]
            val dayToUse = daysInThisMonth.coerceAtMost(date.dayOfMonth)



            val sameDayOfMonth = LocalDate.of(year, month, dayToUse)
            sameDays.add(sameDayOfMonth)



        }

        return sameDays.sorted()
    }

    fun setSunType(sunType: String){
        _tableUIState.update{ currentState ->
            currentState.copy(
                chosenSunType = sunType,
            )
        }
    }
/*
    fun setTimeZone(timezone: Double){
        _tableUIState.update { currentState ->
            currentState.copy(
                timeZoneOffset = timezone
            )
        }
    }

 */

    fun setLocationSearchQuery(inputQuery: String) {
        _tableUIState.update { currentState ->
            currentState.copy(
                locationSearchQuery = inputQuery
            )
        }
    }

    fun loadLocationSearchResults(query: String) {
        viewModelScope.launch {
            try {
                val locationSearchResults = dataSource.fetchLocationSearchResults(query, 10)

                _tableUIState.update { currentState ->
                    currentState.copy(
                        locationSearchResults = locationSearchResults
                    )
                }

            } catch (e: Throwable) {
                Log.d("error", "uh oh$e")
            }
        }
    }


    private fun setTimeZoneOffset(timeZoneOffset: Double, timezone_id: String) {
        _tableUIState.update { currentState ->
            currentState.copy(
                timeZoneOffset = timeZoneOffset,
                timezone_id = timezone_id
            )
        }
    }

    fun setCoordinates(newLatitude: Double, newLongitude: Double, setTimeZoneOffset: Boolean) {
        viewModelScope.launch {
            if(setTimeZoneOffset) {
                val locationTimeZoneOffsetResult = dataSource.fetchLocationTimezoneOffset(newLatitude, newLongitude)
                setTimeZoneOffset(locationTimeZoneOffsetResult.offset, locationTimeZoneOffsetResult.timezone_id)
            }


            _tableUIState.update { currentState ->
                currentState.copy(
                    location = Location("").apply {
                        latitude = newLatitude
                        longitude = newLongitude
                    }
                )
            }
            loadSunInformation()
        }
    }
    fun updateDay(day: Int){
        setNewDate(_tableUIState.value.chosenDate.year, _tableUIState.value.chosenDate.monthValue, day)

    }

    fun updateMonth(month: Int, maxDate: Int){
        var day = _tableUIState.value.chosenDate.dayOfMonth
        if (maxDate < day){
            day = maxDate
        }
        setNewDate(_tableUIState.value.chosenDate.year, month, day)
    }

    fun updateYear(year: Int){
        setNewDate(year, _tableUIState.value.chosenDate.monthValue, _tableUIState.value.chosenDate.dayOfMonth)
    }

    private fun setNewDate(year: Int, month: Int, day: Int){
        _tableUIState.update { currentState ->
            currentState.copy(
                chosenDate = LocalDateTime.of(year, month, day, 12, 0, 0)
            )

        }
        loadSunInformation()
    }

    private fun findOffset(location: String, date: String): Double {
       val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
           val calendar = Calendar.getInstance()
           val dateTime = dateFormat.parse(date)
        if (dateTime != null) {
            calendar.time = dateTime
        }
           val timeZone = TimeZone.getTimeZone(location)
           val offsetInMillis = timeZone.getOffset(calendar.timeInMillis)
           val offsetHours = offsetInMillis / (1000 * 60 * 60)
           return offsetHours.toDouble()
    }


    fun convertToMinutesFunction(minutes: String): String{
        var doubleMinutes = "0.$minutes".toDouble()
        var minutes = doubleMinutes * 60
        return minutes.toString().split(".")[0]



    }






}



