package com.example.sunandmoon.ui.screens

import android.annotation.SuppressLint
import android.location.Location
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
import com.example.sunandmoon.data.util.LocationAndDateTime
import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.ui.components.NavigationComposable
import com.example.sunandmoon.ui.components.TableCard
import com.example.sunandmoon.viewModel.TableViewModel
import java.time.LocalDateTime
import java.util.*


@Composable
fun TableScreen(navigateToNextBottomBar: (index: Int) -> Unit, modifier: Modifier, tableViewModel: TableViewModel = viewModel()) {
    val tableUiState by tableViewModel.tableUiState.collectAsState()

    TableView(modifier,tableViewModel, tableUiState, navigateToNextBottomBar)
}



@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableView(modifier: Modifier,tableViewModel: TableViewModel = viewModel(), tableUIState: TableUIState, navigateToNextBottomBar: (index: Int) -> Unit) {


    //tableViewModel.loadDateTableList(sunType = "Sunrise")
    //tableViewModel.loadDateTableList(sunType = tableUIState.chosenSunType)
    Scaffold(modifier = modifier.fillMaxSize(),
        content = { innerPadding ->
            val padding = innerPadding
            Column(modifier.fillMaxSize()) {

                var chosenSunType = ""

                Spacer(modifier = modifier.height(40.dp))

                Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                    Spacer(modifier = modifier.width(10.dp))
                    chosenSunType = dropdownMenuSunType(tableViewModel, modifier)


                    Log.d("tableUiStateUnit", tableUIState.chosenSunType)
                    Log.d("chosenUnitDropDown", chosenSunType)


                    //tableViewModel.loadDateTableList(sunType = tableUIState.chosenSunType)
                    Spacer(modifier = modifier.width(10.dp))
                }

                Spacer(modifier = modifier.height(20.dp))


                Column(modifier = modifier.fillMaxWidth()) {
                    // Render the header row
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .background(Color.LightGray)
                    ) {
                        Text(
                            text = "Day",
                            modifier = modifier
                                .weight(1f)
                                .padding(8.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = chosenSunType,
                            modifier = modifier
                                .weight(1f)
                                .padding(8.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Our $chosenSunType",
                            modifier = modifier
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
                        modifier = modifier.fillMaxSize()
                    ) {
                        var i = 0
                        items(apiDateTableList) { date ->
                            //println(date)

                            println(apiDateTableList.size)
                            var elementInTableUiStateList = date.split("T")

                            var sunriseTime = elementInTableUiStateList[1]
                            var day = elementInTableUiStateList[0]
                            var monthInt = day.split("-")[1].toInt()
                            println(monthInt)

                            println("Our CalculationsTime: " + tableUIState.calculationsDateTableList[monthInt-1])

                            TableCard(
                                apiSunTime = sunriseTime,
                                day = day,
                                calculationSunTime = tableUIState.calculationsDateTableList[monthInt-1],
                                modifier = modifier
                                    .background(if (date.indexOf(day) % 2 == 0) Color.White else Color.LightGray)
                                    .padding(8.dp)
                            )

                            i+=1


                        }

                    }
                    Spacer(modifier = modifier.height(200.dp))
                }

            }
        },
        bottomBar = {
            NavigationComposable(page = 2, navigateToNextBottomBar)
        }



    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dropdownMenuSunType(tableViewModel: TableViewModel = viewModel(), modifier: Modifier): String{
    //Dropdown
    val options = stringArrayResource(com.example.sunandmoon.R.array.suntype)
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("Sunrise") }


    //Dropdown menu setup
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }, modifier = modifier.width(350.dp)
    ) {
        TextField(
            modifier = modifier.menuAnchor(),
            // The `menuAnchor` modifier must be passed to the text field for correctness.

            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text("Type")},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                //cursorColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                containerColor =  MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.onPrimary
            ),
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
