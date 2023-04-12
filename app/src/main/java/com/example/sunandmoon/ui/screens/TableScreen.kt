package com.example.sunandmoon.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.data.TableUIState
import com.example.sunandmoon.getSunRiseNoonFall
import com.example.sunandmoon.ui.components.NavigationComposable
import com.example.sunandmoon.ui.components.TableCard
import com.example.sunandmoon.viewModel.TableViewModel
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.JsonNull.content
import java.util.*


@Composable
fun TableScreen(navigateToNext: () -> Unit, modifier: Modifier, tableViewModel: TableViewModel = viewModel()) {

    val tableUiState by tableViewModel.tableUiState.collectAsState()

    TableView(modifier,tableViewModel, tableUiState, navigateToNext)

    //Log.d("List: ", tableUIState.dateTableList.toString())

}



@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableView(modifier: Modifier,tableViewModel: TableViewModel = viewModel(), tableUIState: TableUIState, navigateToNext: () -> Unit) {


    //tableViewModel.loadDateTableList(sunType = "Sunrise")
    //tableViewModel.loadDateTableList(sunType = tableUIState.chosenSunType)
    Scaffold(modifier = modifier.fillMaxSize(),
        content = { innerPadding ->
            val padding = innerPadding
            Column(Modifier.fillMaxSize()) {

                var chosenSunType = ""

                Spacer(modifier = Modifier.height(40.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                    Spacer(modifier = Modifier.width(10.dp))
                    chosenSunType = dropdownMenuSunType(tableViewModel)


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
                    var apiDateTableList = tableUIState.apiDateTableList
                    println("api:" + apiDateTableList)
                    println("calculations:" + tableUIState.calculationsDateTableList)


                    // Render the table rows
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        var i = 0
                        items(apiDateTableList) { date ->
                            //println(date)

                            println(apiDateTableList.size)
                            var elementInTableUiStateList = date.split("T")

                            var sunriseTime = elementInTableUiStateList[1]
                            var day = elementInTableUiStateList[0]
                            var monthInt = day.split("-")[1].toInt()

                            TableCard(
                                apiSunTime = sunriseTime,
                                day = day,
                                calculationSunTime = tableUIState.calculationsDateTableList[monthInt-1],
                                modifier = Modifier
                                    .background(if (date.indexOf(day) % 2 == 0) Color.White else Color.LightGray)
                                    .padding(8.dp)
                            )

                            i+=1
                        }

                    }
                    Spacer(modifier = Modifier.height(200.dp))
                }

            }
        },
        bottomBar = {
            NavigationComposable(page = 1, navigateToNext)
        }



    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dropdownMenuSunType(tableViewModel: TableViewModel = viewModel()): String{
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
                        tableViewModel.setSunType(selectedOptionText)
                        tableViewModel.loadSunInformation()


                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }

    }
    Log.d("dropdownSelectedOptionText", selectedOptionText)

    return selectedOptionText                                                                                            
}








