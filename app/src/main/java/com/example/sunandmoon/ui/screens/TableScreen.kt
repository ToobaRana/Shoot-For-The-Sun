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
import com.example.sunandmoon.data.TableUIState
import com.example.sunandmoon.model.SunState
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
fun TableScreen(tableViewModel: TableViewModel = viewModel()){
    val sunUiState by tableViewModel.sunUiState.collectAsState()
    val tableUiState by tableViewModel.tableUiState.collectAsState()

    Log.d("sunset", sunUiState.sunSetTime)
    Log.d("sunrise", sunUiState.sunRiseTime)
    Log.d("solarNoon", sunUiState.solarNoon)

    TableView(tableViewModel, tableUiState)

    //Log.d("List: ", tableUIState.dateTableList.toString())

}



@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableView(tableViewModel: TableViewModel, tableUIState: TableUIState) {


    //tableViewModel.loadDateTableList(sunType = "Sunrise")
    //tableViewModel.loadDateTableList(sunType = tableUIState.chosenSunType)

    Column(Modifier.fillMaxSize()) {

        var chosenSunType = ""

        Spacer(modifier = Modifier.height(40.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            Spacer(modifier = Modifier.width(10.dp))
            chosenSunType = dropdownMenuSunType(tableViewModel)
            tableUIState.chosenSunType = chosenSunType
            Log.d("tableUiStateUnit", tableUIState.chosenSunType)
            Log.d("chosenUnitDropDown", chosenSunType)



            //tableViewModel.loadDateTableList(sunType = tableUIState.chosenSunType)
            Spacer(modifier = Modifier.width(10.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))


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
            var dateTableList = tableUIState.dateTableList
            println(dateTableList)



            // Render the table rows
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                items(dateTableList) { date ->


                    var elementInTableUiStateList = date.split("T")

                    var sunriseTime = elementInTableUiStateList[1]
                    var day = elementInTableUiStateList[0]


                    TableCard(
                        sunTime = sunriseTime,
                        day = day,
                        chosenSunType = chosenSunType,
                        modifier = Modifier
                            .background(if (date.indexOf(day) % 2 == 0) Color.White else Color.LightGray)
                            .padding(8.dp)
                    )
                }

            }
        }
    }
}



/*
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

    return selectedOptionText
}

 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dropdownMenuSunType(tableViewModel: TableViewModel): String{
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
                        tableViewModel.loadDateTableList(sunType = selectedOptionText)

                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }

    }
    Log.d("dropdownSelectedOptionText", selectedOptionText)

    return selectedOptionText                                                                                            
}








