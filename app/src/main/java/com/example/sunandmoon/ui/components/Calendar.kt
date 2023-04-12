package com.example.sunandmoon.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.viewModel.SunViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*


val calenderDayHeight = 5
val numWeekdays = 7
val sumMonths = 12

val months = listOf<String>(
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
)

val amountDays: HashMap<String, Int> = hashMapOf<String, Int>(
    "January" to 31,
    "February" to 28,
    "March" to 31,
    "April" to 30,
    "May" to 31,
    "June" to 30,
    "July" to 31,
    "August" to 31,
    "September" to 30,
    "October" to 31,
    "November" to 30,
    "December" to 31
)
val weekdays =
    listOf<String>("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")


@Composable
fun CalendarComponent(modifier: Modifier, sunViewModel: SunViewModel = viewModel()) {
    var showCalendar by remember { mutableStateOf(false) }
    Button(onClick = { showCalendar = !showCalendar }) {
        Text(text = "Show Calendar")
    }
    if (showCalendar) {
        CalendarComponentDisplay(modifier, sunViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarComponentDisplay(modifier: Modifier, sunViewModel: SunViewModel = viewModel()) {

    val sunUIState by sunViewModel.sunUiState.collectAsState()

    var currentYear by remember { mutableStateOf("2023") }
    Card(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.6f)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.size(30.dp))
                Box {
                    monthDropDown(sunUIState.currentMonth)
                }

                TextField(
                    modifier = Modifier.fillMaxSize(0.7f),
                    value = currentYear,
                    onValueChange = { year ->
                        if(year.isNotEmpty()){
                            if (year[year.length - 1].isDigit() && year.length <= 4) {
                                currentYear = year
                                Log.v("ÅR", currentYear);

                            }
                        }
                        else{
                            currentYear = ""
                        }


                        Log.v("ÅR", year);
                    },

                    label = { Text("Year") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                drawWeekdays(numWeekdays)

            }
            val daysBeforeFirst = weekdays.indexOf(
                getDayOfFirst(
                    month = (sunUIState.currentMonth + 1).toString(),
                    year = currentYear
                )
            )



            for (i in 0..calenderDayHeight) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (i == 0) {
                        for (y in 0 until daysBeforeFirst) {
                            Spacer(
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(1.dp)
                            )
                        }
                        for (y in daysBeforeFirst until numWeekdays) {
                            val date = (y + 1 - daysBeforeFirst)
                            val selected = false
                            Box(modifier = Modifier
                                .size(50.dp)
                                .padding(1.dp)
                                .clickable { sunViewModel.setNewDate(date); },

                                //bruk en array med farger/tall for å endre. Ha referanser til farger slik at det er mulig å endre på de
                                contentAlignment = Alignment.Center
                            ) {
                                Text(

                                    text = date.toString(),
                                    fontSize = 20.sp,


                                    )
                            }
                        }
                    } else {
                        for (y in 0 until numWeekdays) {
                            val date = (y + (i * 7) + 1) - daysBeforeFirst

                            if (date > amountDays[months[sunUIState.currentMonth]]!!) {
                                Spacer(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(1.dp)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(1.dp)
                                        .clickable { sunViewModel.setNewDate(date) },

                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(

                                        text = date.toString(),
                                        fontSize = 20.sp,


                                        )
                                }
                            }

                        }
                    }


                }
            }
        }

    }
}

@Composable
fun drawWeekdays(numDays: Int) {
    for (y in 0 until numWeekdays) {
        Text(text = weekdays[y].subSequence(0, 3).toString())
    }
}

//dropdown-menu for choosing month
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun monthDropDown(currentMonth: Int, sunViewModel: SunViewModel = viewModel()) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {

        TextField(

            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(0.4f),
            readOnly = true,
            value = months[currentMonth],
            onValueChange = { expanded = !expanded },
            label = { Text("Month") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = true) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(

            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            months.forEach { selectionOption ->
                DropdownMenuItem(

                    onClick = {


                        sunViewModel.updateMonth(months.indexOf(selectionOption))



                        expanded = false

                    },
                    text = { Text(text = selectionOption) }
                )
            }
        }
    }

}

//returns which day the first of any month or year falls on
fun getDayOfFirst(month: String, year: String): String {

    val maskedYear = mask(value = year, type = "year")
    val maskedMonth = mask(value = month, type = "month")

    val dateString = "${maskedYear}-$maskedMonth-01"
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    val date = LocalDate.parse(dateString, formatter)
    return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())

}

//masking dates for getDayOfFirst-call
fun mask(value: String, type: String): String {
    when (type) {
        "year" -> {
            val yearMask = "0000"
            return yearMask.substring(0, yearMask.length - value.length) + value

        }
        "month" -> {
            val monthMask = "00"
            return monthMask.substring(0, monthMask.length - value.length) + value
        }

    }
    return "not valid"
}

