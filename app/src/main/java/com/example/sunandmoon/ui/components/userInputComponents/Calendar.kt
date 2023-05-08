package com.example.sunandmoon.ui.components


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalDateTime
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
fun CalendarComponent(
    modifier: Modifier,
    chosenDate: LocalDateTime,
    updateYear: (year: Int) -> Unit,
    updateDay: (day: Int) -> Unit,
    updateMonth: (month: Int, maxDays: Int) -> Unit
) {

    var showCalendar by remember { mutableStateOf(false) }
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        Button(onClick = { showCalendar = !showCalendar}) {
            Text(text = "Choose date")
        }
        if (showCalendar) {
            CalendarComponentDisplay(modifier, chosenDate, updateYear, updateDay, updateMonth)
        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarComponentDisplay(
    modifier: Modifier,
    chosenDate: LocalDateTime,
    updateYear: (year: Int) -> Unit,
    updateDay: (day: Int) -> Unit,
    updateMonth: (month: Int, maxDays: Int) -> Unit
) {


    //var currentYear by remember { mutableStateOf("2023") }

    // vi har lyst til å prøve å la deg ha en blank tekstfelt for år
    val currentYear: Int = chosenDate.year
    var currentYearText: String by remember {
        mutableStateOf(currentYear.toString())
    }
    if (currentYear == 0) {
        currentYearText = ""
    }

    val focusManager = LocalFocusManager.current

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.primary
        )


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
                    monthDropDown(modifier, chosenDate.monthValue, updateMonth)
                }
                Spacer(modifier.size(30.dp))
                Box(modifier = modifier.wrapContentSize(Alignment.Center, false)) {


                    TextField(
                        modifier = modifier.fillMaxSize(0.7f),
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
                                if (currentYearText.isEmpty()) {
                                    updateYear(0)
                                } else {
                                    updateYear(currentYearText.toInt())
                                    focusManager.clearFocus()
                                }


                            }
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = MaterialTheme.colorScheme.primary,
                            textColor = MaterialTheme.colorScheme.primary,
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            selectionColors = TextSelectionColors(
                                handleColor = MaterialTheme.colorScheme.primary,
                                backgroundColor = MaterialTheme.colorScheme.background
                            )
                        )

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
                    month = (chosenDate.monthValue),
                    year = currentYear
                )
            )
            val calenderDayHeight =
                (amountDays[months[chosenDate.monthValue - 1]]!! + daysBeforeFirst - 1) / 7


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
                            DrawDayBox(modifier, day, chosenDate.dayOfMonth) { updateDay(day) }
                        }
                    } else {
                        for (y in 0 until numWeekdays) {
                            val day = (y + (i * 7) + 1) - daysBeforeFirst

                            if (day > amountDays[months[chosenDate.monthValue - 1]]!!) {
                                Spacer(
                                    modifier = modifier
                                        .size(50.dp)
                                        .padding(1.dp)
                                )

                            } else {
                                DrawDayBox(modifier, day, chosenDate.dayOfMonth) { updateDay(day) }


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
fun monthDropDown(
    modifier: Modifier,
    currentMonth: Int,
    updateMonth: (month: Int, maxDays: Int) -> Unit
) {
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
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.textFieldColors(
                //cursorColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.tertiary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                disabledTrailingIconColor = MaterialTheme.colorScheme.primary,
                errorTrailingIconColor = MaterialTheme.colorScheme.primary,
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            //modifier = modifier.background(MaterialTheme.colorScheme.tertiary)
        ) {
            months.forEach { selectionOption ->
                DropdownMenuItem(

                    onClick = {


                        updateMonth(
                            months.indexOf(selectionOption) + 1,
                            amountDays[selectionOption]!!
                        )
                        //fetch date, update month in uistate


                        expanded = false

                    },
                    text = {
                        Text(
                            text = selectionOption,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
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

@Composable
fun DrawDayBox(modifier: Modifier, day: Int, chosenDay: Int, updateDay: () -> Unit) {
    val brush = Brush.horizontalGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary
        )
    )

    var usedModifier = modifier
    if (day == chosenDay) {
        usedModifier = modifier.border(BorderStroke(2.dp, brush), RoundedCornerShape(5.dp))
    }
    Box(
        modifier = usedModifier
            .size(50.dp)
            .padding(1.dp)
            .clickable() { updateDay(); },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            fontSize = 20.sp,
        )
    }
}