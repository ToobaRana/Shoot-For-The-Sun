package com.example.sunandmoon.ui.components

import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.data.SunUiState
import com.example.sunandmoon.viewModel.SunViewModel
import kotlinx.coroutines.flow.StateFlow
import java.time.DayOfWeek

class Calendar(private val modifier: Modifier, uiState: SunUiState) {
    val calenderDayHeight = 5
    val numWeekdays = 6
    val sumMonths = 12
    val uiState = uiState
    var currentYear = "2023"
    val months = listOf<String>(
        "Choose",
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
    val weekdays = listOf<String>("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CalendarComponent(viewModel: SunViewModel = viewModel()) {
        var word by remember { mutableStateOf("") }
        Card(modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(0.5f)) {

            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.size(30.dp))
                    Box() {
                        var expanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded },
                        ) {

                            TextField(

                                modifier = Modifier
                                    //.menuAnchor()
                                    .fillMaxWidth(0.4f),
                                readOnly = true,
                                value = months[uiState.currentMonth],
                                onValueChange = { expanded = false },
                                label = { Text("Month") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            )
                            ExposedDropdownMenu(

                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                            ) {
                                months.forEach { selectionOption ->
                                    DropdownMenuItem(

                                        onClick = {


                                            viewModel.updateMonth(months.indexOf(selectionOption))



                                            expanded = false

                                        },
                                        text = { Text(text = selectionOption) }
                                    )
                                }
                            }
                        }
                    }

                        TextField(
                            modifier = Modifier.fillMaxSize(0.7f),
                            value = word,
                            onValueChange = { year ->
                                currentYear = year; Log.v(
                                "ÅR",
                                currentYear
                            ); word = year
                            },

                            label = { Text("Year") }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (y in 0 until numWeekdays) {
                            Text(text = weekdays[y])
                        }

                    }
                    for (i in 0..calenderDayHeight) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            for (y in 0 until numWeekdays) {
                                Text(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(1.dp)
                                        .clickable { viewModel.setNewDate(y + (i * 7) + 1) },
                                    text = (y + (i * 7) + 1).toString(),
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,

                                    )


                            }


                        }
                    }
                }

            }
        }

    }
