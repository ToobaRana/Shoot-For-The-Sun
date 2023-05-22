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
import com.example.sunandmoon.ui.components.userInputComponents.dropdownMenuSunType
import com.example.sunandmoon.viewModel.ProductionSelectionViewModel
import com.example.sunandmoon.viewModel.TableViewModel
import okhttp3.internal.format
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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
                modifier = modifier.fillMaxWidth()
                    .padding(top = 15.dp, end = 5.dp),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight(400),
                minLines = 2,
                lineHeight = 35.sp
            )
        },

        content = { innerPadding ->
            innerPadding
            Column(
                modifier.padding(top = 120.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LocationSearch(
                        modifier = modifier.width(200.dp).padding(start = 1.dp),
                        locationSearchQuery = tableUIState.locationSearchQuery,
                        locationSearchResults = tableUIState.locationSearchResults,
                        setLocationSearchQuery = { query: String, format: Boolean ->
                            tableViewModel.setLocationSearchQuery(query, format)
                        },
                        loadLocationSearchResults = { query: String ->
                            tableViewModel.loadLocationSearchResults(query)
                        },
                        setCoordinates = { newLocation, setTimeZoneOffset: Boolean ->
                            tableViewModel.setCoordinates(
                                newLocation,
                                setTimeZoneOffset
                            )
                        }
                    )
                    chosenSunType = dropdownMenuSunType(tableViewModel, modifier = Modifier.width(165.dp).padding(end = 1.dp))
                }
                Spacer(modifier = modifier.height(10.dp))
            }

            Box(modifier.padding(top=35.dp)){

                Box(modifier
                        .fillMaxSize()
                        .padding(top = 300.dp, start = 2.5.dp, end = 2.5.dp, bottom = 84.dp)) {

                LazyColumn(modifier.padding(top = 15.dp)) {

                    item { Row(
                        modifier.background(MaterialTheme.colorScheme.tertiary).padding(top= 5.dp, bottom=5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Date",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = modifier.weight(1.5f).padding(start = 10.dp),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "API",
                            fontSize = 20.sp,
                            modifier = modifier
                                .weight(0.9f),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Ours",
                            fontSize = 20.sp,
                            modifier = modifier
                                .weight(0.9f),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Offset",
                            fontSize = 20.sp,
                            modifier = modifier
                                .weight(0.9f),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                    } }

                    items(tableUIState.apiDateTableList) { date ->

                        val elementInTableUiStateList = date.split("T")
                        Log.i("elementInTableUiStateList", elementInTableUiStateList.toString())
                        val sunriseTimeListWithOffset = elementInTableUiStateList[1].split("-", "+")
                        val sunriseTime = sunriseTimeListWithOffset[0]
                        val day = elementInTableUiStateList[0] //"${dateObject.dayOfMonth}. ${dateObject.month.toString().substring(0, 3)} ${dateObject.year}"
                        val monthInt = day.split("-")[1].toInt()

                        val dateObject: LocalDate = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        var dag = "${dateObject.dayOfMonth}. ${dateObject.month.toString().substring(0, 3)} ${dateObject.year}"

                        TableCard(
                            apiSunTime = sunriseTime,
                            day = dag,
                            calculationSunTime = tableUIState.calculationsDateTableList[monthInt-1],
                            offset = tableUIState.timeZoneListTableScreen[monthInt-1],
                            modifier = modifier
                                .background(if (date.indexOf(day) % 2 == 0) Color.White else Color.LightGray)
                                .padding(2.dp)
                        )

                        Spacer(modifier = modifier.height(1.dp))

                    }
                }

            }
                Box(
                    modifier
                        .fillMaxWidth()
                        .padding(top = 180.dp)
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

            NavigationComposable(modifier = modifier, page = 2, navigateToNextBottomBar)
        }


    )

}

/*
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
        onExpandedChange = { expanded = !expanded }, modifier = modifier.width(310.dp)
    ) {

        TextField(
            modifier = modifier.menuAnchor(),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text("Type", color = MaterialTheme.colorScheme.primary, fontSize = 18.sp) },
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
            ), maxLines = 1
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = modifier.background(MaterialTheme.colorScheme.tertiary)
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption, fontSize = 18.sp) },
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
    //Log.d("dropdownSelectedOptionText", selectedOptionText)

    return selectedOptionText
}

 */
