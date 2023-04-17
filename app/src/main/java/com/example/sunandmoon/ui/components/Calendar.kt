package com.example.sunandmoon.ui.components

import android.graphics.Paint.Align
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.viewModel.SunViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*


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

    //var currentYear by remember { mutableStateOf("2023") }

    // vi har lyst til å prøve å la deg ha en blank tekstfelt for år
    val currentYear: Int = sunUIState.chosenDate.year
    var currentYearText: String by remember {
        mutableStateOf(currentYear.toString())
    }
    if (currentYear == 0) {
        currentYearText = ""
    }



    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier,


        ) {

        Column(
            modifier = modifier
                .wrapContentSize(Alignment.Center, false)
                .fillMaxWidth(0.95f)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(80.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier.size(30.dp))
                Box {
                    monthDropDown(modifier, sunUIState.chosenDate.monthValue)
                }
                Spacer(modifier.size(30.dp))
                Box(modifier = modifier.wrapContentSize(Alignment.Center, false)) {
                    val focusManager = LocalFocusManager.current
                    TextField(
                        modifier = modifier.fillMaxSize(0.7f)/*.onKeyEvent { type -> if(type.type == KeyEventType.KeyUp) }*/,
                        value = currentYearText,
                        onValueChange = { year: String ->
                            if (year.isNotEmpty()) {
                                if (year[year.length - 1].isDigit() && year.length <= 4) {

                                    currentYearText = year.replace("\\D".toRegex(), "")


                                    Log.v("ÅR", year);

                                }
                            } else {
                                currentYearText = ""
                            }


                            Log.v("ÅR", year);
                        },

                        placeholder = { Text(text = "0") },

                        label = { Text("Year") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                sunViewModel.updateYear(currentYearText.toInt())
                                focusManager.clearFocus()
                            })

                    )
                    Spacer(modifier.size(30.dp))
                }
            }
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                drawWeekdays(numWeekdays)

            }
            val daysBeforeFirst = weekdays.indexOf(
                getDayOfFirst(
                    month = (sunUIState.chosenDate.monthValue),
                    year = currentYear
                )
            )
            val calenderDayHeight =
                (amountDays[months[sunUIState.chosenDate.monthValue - 1]]!! + daysBeforeFirst - 1) / 7



            for (i in 0..calenderDayHeight) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (i == 0) {
                        for (y in 0 until daysBeforeFirst) {
                            Spacer(
                                modifier = modifier
                                    .size(50.dp)
                                    .padding(1.dp)
                            )
                        }
                        for (y in daysBeforeFirst until numWeekdays) {
                            val day = (y + 1 - daysBeforeFirst)
                            val selected = false
                            Box(
                                modifier = modifier
                                    .size(50.dp)
                                    .padding(1.dp)
                                    .clickable { sunViewModel.updateDay(day); },

                                //bruk en array med farger/tall for å endre. Ha referanser til farger slik at det er mulig å endre på de
                                contentAlignment = Alignment.Center
                            ) {
                                Text(

                                    text = day.toString(),
                                    fontSize = 20.sp,


                                    )
                            }
                        }
                    } else {
                        for (y in 0 until numWeekdays) {
                            val day = (y + (i * 7) + 1) - daysBeforeFirst

                            if (day > amountDays[months[sunUIState.chosenDate.monthValue - 1]]!!) {
                                Spacer(
                                    modifier = modifier
                                        .size(50.dp)
                                        .padding(1.dp)
                                )

                            } else {
                                Box(
                                    modifier = modifier
                                        .size(50.dp)
                                        .padding(1.dp)
                                        .clickable { sunViewModel.updateDay(day) },

                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(

                                        text = day.toString(),
                                        fontSize = 20.sp,


                                        )

                                }


                            }

                        }
                    }


                }
            }
            Spacer(modifier.size(30.dp))
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
fun monthDropDown(modifier: Modifier, currentMonth: Int, sunViewModel: SunViewModel = viewModel()) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {

        TextField(

            modifier = modifier
                .menuAnchor()
                .fillMaxWidth(0.6f),
            readOnly = true,
            value = months[currentMonth - 1],
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


                        sunViewModel.updateMonth(months.indexOf(selectionOption) + 1)
                        //fetch date, update month in uistate


                        expanded = false

                    },
                    text = { Text(text = selectionOption) }
                )
            }
        }
    }

}

//returns which day the first of any month or year falls on
fun getDayOfFirst(month: Int, year: Int): String {


    val date = LocalDate.of(year, month, 1)

    return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())

}

//masking dates for getDayOfFirst-call
