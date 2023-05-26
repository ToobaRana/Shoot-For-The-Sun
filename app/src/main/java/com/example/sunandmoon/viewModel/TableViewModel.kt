package com.example.sunandmoon.viewModel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.TableUIState
import com.example.sunandmoon.getSunRiseNoonFall
import com.example.sunandmoon.util.simplifyLocationNameQuery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
            timezone_id = "Europe/Oslo",
            offsetStringForApi = "02:00",
            timeZoneListTableScreen = listOf("", "", "", "", "", "", "", "", "", "", "", ""),
            setSameDaysFromJanuaryList = listOf()
        )
    )

    val tableUIState: StateFlow<TableUIState> = _tableUIState.asStateFlow()

    init {
        loadTableSunInformation()
    }

    fun loadTableSunInformation(){
        viewModelScope.launch {

            val apiDateTableList = mutableListOf<String?>()
            val calculationsDateTableList = mutableListOf<String>()
            val timeZoneListTableScreen = mutableListOf<String>()

            val sameDaysList = getSameDaysInYear(tableUIState.value.chosenDate.toLocalDate())
            val sameDaysListFromJanuary = getSameDaysInYearFromJanuary(tableUIState.value.chosenDate.toLocalDate())
            setSameDaysFromJanuaryList(sameDaysListFromJanuary)

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

            for (date in sameDaysListFromJanuary.sorted()){
                val offsetToFindSignDouble = findOffset(tableUIState.value.timezone_id, date.toString())
                val offsetString: String = formatTheOffset(offsetToFindSignDouble)

                //update UiState
                setTimeZoneStringApi(offsetString)
                timeZoneListTableScreen.add(offsetString)

                val stringDate = date.toString() + " 12:00"

                val dateTime = LocalDateTime.parse(stringDate, formatter)

                val calculationSunTime = getSunRiseNoonFall(dateTime, offsetToFindSignDouble, tableUIState.value.location)

                if (tableUIState.value.chosenSunType == "Sunrise"){
                    calculationsDateTableList.add(calculationSunTime[0].format(timeFormatter))
                }

                if (tableUIState.value.chosenSunType == "SolarNoon"){
                    calculationsDateTableList.add(calculationSunTime[1].format(timeFormatter))
                }

                if (tableUIState.value.chosenSunType == "Sunset"){
                    calculationsDateTableList.add(calculationSunTime[2].format(timeFormatter))
                }

            }

            try {

                for (date in sameDaysList.sorted()){
                    val offsetToFindSignDouble = findOffset(tableUIState.value.timezone_id, date.toString())

                    val offsetString: String = formatTheOffset(offsetToFindSignDouble)

                    if (tableUIState.value.chosenSunType == "Sunrise"){
                        val sunRiseTime = dataSource.fetchSunrise3Data("sun", tableUIState.value.location.latitude, tableUIState.value.location.longitude, date.toString(), offsetString).properties.sunrise.time

                        apiDateTableList.add(sunRiseTime)

                    }

                    if (tableUIState.value.chosenSunType == "SolarNoon"){
                        val solarNoon = dataSource.fetchSunrise3Data("sun", tableUIState.value.location.latitude, tableUIState.value.location.longitude, date.toString(), offsetString).properties.solarnoon.time

                        apiDateTableList.add(solarNoon)

                    }

                    if (tableUIState.value.chosenSunType == "Sunset"){
                        val sunSetTime = dataSource.fetchSunrise3Data("sun", tableUIState.value.location.latitude, tableUIState.value.location.longitude, date.toString(), offsetString).properties.sunset.time
                        apiDateTableList.add(sunSetTime)
                    }
                }

                _tableUIState.update{currentState ->
                    currentState.copy(
                        apiDateTableList = apiDateTableList,
                        calculationsDateTableList = calculationsDateTableList,
                        timeZoneListTableScreen = timeZoneListTableScreen,
                        missingNetworkConnection = false
                    )
                }

            } catch (_: Exception) {
                _tableUIState.update{currentState ->
                    currentState.copy(
                        missingNetworkConnection = true
                    )
                }
            }
        }
    }

    private fun getSameDaysInYear(date: LocalDate): List<LocalDate> {

        val daysInMonth =
            if (date.isLeapYear){
                listOf(31, 29, 31, 30, 31, 30, 31, 31 ,30, 31, 30, 31)
            } else {
                listOf(31, 28, 31, 30, 31, 30, 31, 31 ,30, 31, 30, 31)
            }
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
        val daysInMonth =
        if (date.isLeapYear){
            listOf(31, 29, 31, 30, 31, 30, 31, 31 ,30, 31, 30, 31)
        } else {
            listOf(31, 28, 31, 30, 31, 30, 31, 31 ,30, 31, 30, 31)
        }


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
    private fun setTimeZoneStringApi(offsetStringForApi: String){
        _tableUIState.update { currentState ->
            currentState.copy(
                offsetStringForApi = offsetStringForApi,

            )
        }
    }

    fun setLocationSearchQuery(inputQuery: String, format: Boolean) {
        var queryToStore = inputQuery
        if(format) queryToStore = simplifyLocationNameQuery(queryToStore)
        _tableUIState.update { currentState ->
            currentState.copy(
                locationSearchQuery = queryToStore
            )
        }
    }

    fun setSameDaysFromJanuaryList(setSameDaysFromJanuaryList: List<LocalDate>) {
        _tableUIState.update { currentState ->
            currentState.copy(
                setSameDaysFromJanuaryList = setSameDaysFromJanuaryList
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
                _tableUIState.update { currentState ->
                    currentState.copy(
                        missingNetworkConnection = true
                    )
                }
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

    fun setCoordinates(newLocation: Location) {
        viewModelScope.launch {

            val locationTimeZoneOffsetResult = dataSource.fetchLocationTimezoneOffset(newLocation)
            setTimeZoneOffset(locationTimeZoneOffsetResult.offset, locationTimeZoneOffsetResult.timezone_id)

            _tableUIState.update { currentState ->
                currentState.copy(
                    location = newLocation
                )
            }
            loadTableSunInformation()
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
        loadTableSunInformation()
    }

    private fun findOffset(location: String, date: String): Double {
        val dateTime = LocalDateTime.of(convertStringToLocalDate(date), LocalDateTime.now().toLocalTime())
        val zoneId = ZoneId.of(location)
        val zoneOffset = zoneId.rules.getOffset(dateTime)

        val offsetHours = zoneOffset.totalSeconds / 3600
        val offsetMinutes = (zoneOffset.totalSeconds % 3600) / 60

        val offset = offsetHours + (offsetMinutes.toDouble() / 60)
        return offset

    }


    private fun convertToMinutesFunction(minuteParameter: String): String{
        val doubleMinutes = ("0." + minuteParameter).toDouble()
        val minute = doubleMinutes * 60
        return minute.toString().split(".")[0]



    }

    private fun convertStringToLocalDate(dateString: String): LocalDate {

        return LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE)
    }
    fun formatTheOffset(offsetToFindSignDouble: Double): String{
        val offsetString: String
        val offsetList = offsetToFindSignDouble.toString().split(".")
        val hours = offsetList[0]
        val hoursWithOutSign = hours.split("")[2]
        val minutes = offsetList[1]

        val convertToMinutes = convertToMinutesFunction(minutes)

        if (offsetToFindSignDouble in -9.9..9.9) {

            //if it is hours and minutes
            if (convertToMinutes == "0") {

                //if negative and decimal
                offsetString = if(hours.split("")[1] == "-") {
                    "-0" + hoursWithOutSign  + ":00"
                }

                //not negative
                else{
                    "+0" + hours + ":00"
                }
            }

            //not minutes, only hours
            else{
                //if negative
                offsetString = if(offsetToFindSignDouble.toString().split("")[1] == "-") {
                    "-0" + hours + ":" + convertToMinutes
                }
                //not negative
                else{
                    "+0" + hours + ":" + convertToMinutes

                }
            }

            //same as above, but checks for values over 10 og under -10
        } else if (offsetToFindSignDouble in 10.0..25.0 || offsetToFindSignDouble in -25.0..-10.0) {

            if (convertToMinutes == "0") {
                offsetString = if (offsetToFindSignDouble.toString().split("")[1] == "-") {
                    "-" + hours + ":00"
                }

                else {
                    "+" + hours + ":00"
                }
            }

            else{
                offsetString = if(offsetToFindSignDouble.toString().split("")[1] == "-") {
                    "-" + hours + ":" + convertToMinutes
                }
                else{
                    "+" + hours + ":" + convertToMinutes
                }
            }

        }

        else {
            val offsetFromUiState = tableUIState.value.offsetStringForApi
            offsetString = offsetFromUiState
        }

        return offsetString

    }


}



