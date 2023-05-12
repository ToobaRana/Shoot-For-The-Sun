package com.example.sunandmoon.viewModel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.TableUIState
import com.example.sunandmoon.data.util.LocationAndDateTime
import com.example.sunandmoon.getSunRiseNoonFall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Math.min
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.sign

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
            timezone_id = "Europa/Oslo",
            sign = ""
        )
    )

    val tableUIState: StateFlow<TableUIState> = _tableUIState.asStateFlow()

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

            val sameDaysList = getSameDaysInYear(tableUIState.value.chosenDate.toLocalDate())
            val sameDaysListFromJanuary = getSameDaysInYearFromJanuary(tableUIState.value.chosenDate.toLocalDate())


            for (date in sameDaysListFromJanuary.sorted()){
                var stringDate = "$date 00:00"
                var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                var dateTime = LocalDateTime.parse(stringDate, formatter)

                var calculationSunTime = getSunRiseNoonFall(dateTime, tableUIState.value.timeZoneOffset, tableUIState.value.location.latitude, tableUIState.value.location.longitude)

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
                var offsetToFindSign = findOffset(tableUIState.value.timezone_id, date.toString())
                var offserFromUiState = tableUIState.value.timeZoneOffset
                var offsetString: String = ""

                val offsetDecimal = offserFromUiState.toString().split(".")


                var hours = offsetDecimal[0]
                var hoursWithOutSign = hours.split("")[2]
                Log.d("hoursWithOutSign", hoursWithOutSign)

             

                var minutter = offsetDecimal[1]

                Log.d("offset2", hours)

                Log.d("decimal", minutter)


                //startverdi
                if (offserFromUiState == 0.0) {
                    val offsetFromUiState = tableUIState.value.timeZoneOffset.toString().split(".")[0]
                    offsetString = "+0$offsetFromUiState:00"
                }

                ////hvis desimal
                else if (offserFromUiState in -9.9..9.9) {



                    Log.d("offsetdesimal", tableUIState.value.timeZoneOffset.toString())

                    if (minutter == "0") {

                        //hvis minus og desimal
                       if(offsetToFindSign.toString().split("")[1] == "-") {

                           offsetString = "-0${hoursWithOutSign}:00"
                       }
                       //desimal og ikke minus
                       else{
                            offsetString = "+0${hours}:00"
                           }
                    }

                    //desimal, men sjekk minus
                    else{
                          //hvis minus og desimal
                         if(offsetToFindSign.toString().split("")[1] == "-") {

                             offsetString = "-0${hoursWithOutSign}:${minutter}0"
                         }
                         //desimal og ikke minus
                         else{
                             offsetString = "+0${hours}:${minutter}0"

                         }
                    }

                   //hvis offset er mellom 0 og 9, men er desimal og minus
                } else if (offserFromUiState in 10.0..25.0 || offserFromUiState in -25.0..-10.0) {

                          if (minutter.toDouble() == 0.0) {

                              //hvis minus og desimal
                              if (offsetToFindSign.toString().split("")[1] == "-") {
                                  offsetString = "-${hoursWithOutSign}:00"
                              }
                              //desimal og ikke minus
                              else {
                                  offsetString = "+${hours}:00"
                              }

                          }
                          //ikke desimal, men sjekk minus
                          else{
                                //hvis minus og desimal
                               if(offserFromUiState.toString().split("")[1] == "-") {
                                   offsetString = "-${hoursWithOutSign}:${minutter}0"
                               }
                               //desimal og ikke minus
                               else{
                                   offsetString = "+${hours}:${minutter}0"

                               }
                          }

                      }

                //mangler at halvetime blir tatt hensyn paa

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

        return sameDays.sorted()
    }

    fun setSunType(sunType: String){
        _tableUIState.update{ currentState ->
            currentState.copy(
                chosenSunType = sunType,
            )
        }
    }
    fun setSignType(signType: String){
        _tableUIState.update{ currentState ->
            currentState.copy(
                sign = signType,
            )
        }
    }

    fun setTimeZone(timezone: Double){
        _tableUIState.update { currentState ->
            currentState.copy(
                timeZoneOffset = timezone
            )
        }
    }

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
                Log.d("error", "uh oh" + e.toString())
            }
        }
    }

    fun setTimeZoneOffset(timeZoneOffset: Double, timezone_id: String) {
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
                setTimeZoneOffset(locationTimeZoneOffsetResult.offset.toDouble(), locationTimeZoneOffsetResult.timezone_id)
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

    fun setNewDate(year: Int, month: Int, day: Int){
        _tableUIState.update { currentState ->
            currentState.copy(
                chosenDate = LocalDateTime.of(year, month, day, 12, 0, 0)
            )

        }
        loadSunInformation()
    }

    fun findOffset(location: String, date: String): Double {
        val calendar = Calendar.getInstance()
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.calendar = calendar
        val dateTime = dateFormat.parse(date)
        val timeZone = TimeZone.getTimeZone(location)
        val offsetInMillis = timeZone.getOffset(calendar.timeInMillis)
        val offsetHours = offsetInMillis / (1000 * 60 * 60)
        return offsetHours.toDouble()
    }




}



