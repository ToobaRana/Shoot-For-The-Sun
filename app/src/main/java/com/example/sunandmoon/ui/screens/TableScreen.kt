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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.data.TableUIState
import com.example.sunandmoon.ui.components.CalendarComponent
import com.example.sunandmoon.ui.components.NavigationComposable
import com.example.sunandmoon.ui.components.TableCard
import com.example.sunandmoon.viewModel.TableViewModel


@Composable
fun TableScreen(
    navigateToNextBottomBar: (index: Int) -> Unit,
    modifier: Modifier,
    tableViewModel: TableViewModel = viewModel()
) {
    val tableUiState by tableViewModel.tableUIState.collectAsState()

    TableView(modifier, tableViewModel, tableUiState, navigateToNextBottomBar)
}


@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableView(
    modifier: Modifier,
    tableViewModel: TableViewModel = viewModel(),
    tableUIState: TableUIState,
    navigateToNextBottomBar: (index: Int) -> Unit
) {


    //tableViewModel.loadDateTableList(sunType = "Sunrise")
    //tableViewModel.loadDateTableList(sunType = tableUIState.chosenSunType)
    var chosenSunType = ""

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        topBar = {

            Text(
                text = "Comparing our calculations to MET-API",
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .padding(top = 2.dp, end = 5.dp, start = 5.dp),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight(400)
            )

        },

        content = { innerPadding ->
            innerPadding



            Column(
                modifier.padding(start = 8.dp, end = 8.dp, top = 90.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LocationSearch(
                        modifier = modifier.width(200.dp),
                        locationSearchQuery = tableUIState.locationSearchQuery,
                        locationSearchResults = tableUIState.locationSearchResults,
                        setLocationSearchQuery = { query: String ->
                            tableViewModel.setLocationSearchQuery(
                                query
                            )
                        },
                        loadLocationSearchResults = { query: String ->
                            tableViewModel.loadLocationSearchResults(
                                query
                            )
                        },
                        setCoordinates = { newLatitude: Double, newLongitude: Double, setTimeZoneOffset: Boolean ->
                            tableViewModel.setCoordinates(
                                newLatitude,
                                newLongitude,
                                setTimeZoneOffset
                            )
                        }
                    )
                    chosenSunType = dropdownMenuSunType(tableViewModel, modifier)


                }





                Spacer(modifier = modifier.height(10.dp))
            }



            Box{

            //Spacer(modifier = modifier.height(10.dp))

                //modifier.padding(top = 220.dp, start = 4.dp, end = 4.dp, bottom = 84.dp)
                Box(modifier.fillMaxSize().padding(top = 220.dp, start = 4.dp, end = 4.dp, bottom = 84.dp)) {
                // Render the header row

                Spacer(modifier = modifier.height(1.dp))
                LazyColumn(
                ) {
                    item { Row(

                        modifier.background(Color.LightGray)
                    ) {
                        Text(
                            text = "Day",
                            fontSize = 20.sp,

                            modifier = modifier
                                .weight(1f)
                                .padding(3.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = chosenSunType,
                            fontSize = 20.sp,
                            modifier = modifier
                                .weight(1f)
                                .padding(3.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Our $chosenSunType",
                            fontSize = 20.sp,
                            modifier = modifier
                                .weight(1f)
                                .padding(3.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    } }
                    items(tableUIState.apiDateTableList) { date ->

                        val elementInTableUiStateList = date.split("T")

                        val sunriseTime = elementInTableUiStateList[1]
                        val day = elementInTableUiStateList[0]
                        val monthInt = day.split("-")[1].toInt()

                        println("monthInt: $monthInt")



                        TableCard(
                            apiSunTime = sunriseTime,
                            day = day,
                            calculationSunTime = tableUIState.calculationsDateTableList[monthInt - 1],
                            modifier = modifier
                                .background(if (date.indexOf(day) % 2 == 0) Color.White else Color.LightGray)
                                .padding(8.dp)
                        )
                        Spacer(modifier = modifier.height(1.dp))


                    }


                }

            }
                Box(modifier
                    .fillMaxWidth()
                    .padding(top = 165.dp)
                    .zIndex(1f), contentAlignment = Alignment.Center)   {
                    CalendarComponent(
                        modifier,
                        tableUIState.chosenDate,
                        updateYear = { year: Int -> tableViewModel.updateYear(year) },
                        updateMonth = { month: Int, maxDay: Int ->
                            tableViewModel.updateMonth(
                                month,
                                maxDay
                            )
                        },
                        updateDay = { day: Int -> tableViewModel.updateDay(day) })
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
fun dropdownMenuSunType(tableViewModel: TableViewModel = viewModel(), modifier: Modifier): String {
    //Dropdown
    val options = stringArrayResource(com.example.sunandmoon.R.array.suntype)
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("Sunrise") }


    //Dropdown menu setup
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }, modifier = modifier.width(155.dp)
    ) {
        TextField(
            modifier = modifier.menuAnchor(),
            // The `menuAnchor` modifier must be passed to the text field for correctness.

            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text("Type", color = MaterialTheme.colorScheme.primary) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                placeholderColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                disabledTextColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.onBackground,
                focusedLabelColor = MaterialTheme.colorScheme.onPrimary


            ),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = modifier.background(MaterialTheme.colorScheme.tertiary)
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
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }

    }
    Log.d("dropdownSelectedOptionText", selectedOptionText)

    return selectedOptionText
}
