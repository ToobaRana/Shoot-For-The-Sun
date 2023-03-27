package com.example.sunandmoon.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.data.DataSource
import com.example.sunandmoon.data.SunUiState
import com.example.sunandmoon.ui.components.TableCard
import com.example.sunandmoon.viewModel.SunViewModel
import com.example.sunandmoon.viewModel.TableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

import kotlin.math.max


@Composable
fun TableScreen(modifier: Modifier, tableViewModel: TableViewModel = viewModel()){
    val table by tableViewModel.tableUiState.collectAsState()

    Log.d("sunset", table.sunSetTime)
    Log.d("sunrise", table.sunRiseTime)
    Log.d("solarNoon", table.solarNoon)

    TableView(table, tableViewModel)


}





@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableView(sunUiState: SunUiState, tableViewModel: TableViewModel) {

    val monthList = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    val dayInMonthList = mutableListOf<String>()

    Column(Modifier.fillMaxSize()) {

        var chosenMonth = ""
        var chosenSunType = ""

        Spacer(modifier = Modifier.height(40.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Spacer(modifier = Modifier.width(10.dp))
            chosenMonth = dropdownMenuMonth(monthList = monthList)
            Spacer(modifier = Modifier.width(10.dp))
            chosenSunType = dropdownMenuSunType()
            Spacer(modifier = Modifier.width(10.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        dayInMonthList.clear()

        val cal = Calendar.getInstance()
        cal.set(Calendar.MONTH, Month.valueOf(chosenMonth.uppercase(Locale.ROOT)).value - 1)

        val maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val df = SimpleDateFormat("yyyy-MM-dd")

        for (i in 0 until maxDay) {
            cal.set(Calendar.DAY_OF_MONTH, i + 1)
            dayInMonthList.add(df.format(cal.time))
        }

        /*
        trenger en uistate i en ny nywiewmodel som lagrer på alle sunset datoene i en liste

         */

        Column(modifier = Modifier.fillMaxWidth()) {
            // Render the header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
            ) {
                Text(
                    text = "Day",
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = chosenSunType,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Our $chosenSunType",
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }

            // Render the table rows
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(dayInMonthList) { day ->
                    tableViewModel.loadSunInformation(day)

                    //tableViewModel.loadSunInformation2(day, dataSource)
                    TableCard(
                        day = day,
                        chosenSunType = chosenSunType,
                        sunUiState = sunUiState,
                        modifier = Modifier
                            .background(if (dayInMonthList.indexOf(day) % 2 == 0) Color.White else Color.LightGray)
                            .padding(8.dp)
                    )
                }
                //dayInMonthList.clear()
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dropdownMenuMonth(monthList: List<String>): String{
    //Dropdown
    val options = monthList
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(monthList[0]) }





    //Dropdown menu setup
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }, modifier = Modifier.width(200.dp)
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            // The `menuAnchor` modifier must be passed to the text field for correctness.

            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text("Month")},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),

            )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }

    }






    //AlpacaScreen()
    return selectedOptionText
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dropdownMenuSunType(): String{
    //Dropdown
    val options = stringArrayResource(com.example.sunandmoon.R.array.suntype)
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("Sunrise") }





    //Dropdown menu setup
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }, modifier = Modifier.width(350.dp)
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            // The `menuAnchor` modifier must be passed to the text field for correctness.

            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text("Type")},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),

            )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }

    }

    //AlpacaScreen()
    return selectedOptionText                                                                                            
}








