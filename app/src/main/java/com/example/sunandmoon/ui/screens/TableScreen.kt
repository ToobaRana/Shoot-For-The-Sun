package com.example.sunandmoon.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.data.TableUIState
import com.example.sunandmoon.ui.components.userInputComponents.CalendarComponent
import com.example.sunandmoon.ui.components.NavigationComposable
import com.example.sunandmoon.ui.components.infoComponents.TableCard
import com.example.sunandmoon.ui.components.infoComponents.TableMissingInternet
import com.example.sunandmoon.ui.components.userInputComponents.LocationSearch
import com.example.sunandmoon.ui.components.userInputComponents.dropdownMenuSunType
import com.example.sunandmoon.viewModel.TableViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun TableScreen(
    navigateToNextBottomBar: (index: Int) -> Unit,
    modifier: Modifier,
    tableViewModel: TableViewModel = viewModel()
) {
    val tableUiState by tableViewModel.tableUIState.collectAsState()

    if(!tableUiState.missingNetworkConnection) {
        TableView(modifier, tableUiState, navigateToNextBottomBar)
    }
    else {
        TableMissingInternet(modifier, navigateToNextBottomBar)
    }
}


@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableView(
    modifier: Modifier,
    tableUIState: TableUIState,
    navigateToNextBottomBar: (index: Int) -> Unit,
    tableViewModel: TableViewModel = viewModel(),
) {

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        topBar = {

            Text(
                text = stringResource(R.string.Comparing),
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

        content = { innerPadding -> innerPadding //for removing error
            Column(
                modifier.padding(top = 120.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = modifier.fillMaxWidth().padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
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
                        setCoordinates = { newLocation ->
                            tableViewModel.setCoordinates(
                                newLocation
                            )
                        }
                    )
                    dropdownMenuSunType(tableViewModel, modifier = Modifier.width(165.dp).padding(end = 1.dp))
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
                            text = stringResource(R.string.DateTable),
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = modifier.weight(1.5f).padding(start = 10.dp),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.API),
                            fontSize = 20.sp,
                            modifier = modifier
                                .weight(0.9f),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = stringResource(R.string.Ours),
                            fontSize = 20.sp,
                            modifier = modifier
                                .weight(0.9f),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = stringResource(R.string.Offset),
                            fontSize = 20.sp,
                            modifier = modifier
                                .weight(0.9f),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                    } }

                    //going through the apiDateTableList from UiState
                    //date is consisted of both the sun time and date from API
                    items(tableUIState.apiDateTableList) { date ->
                        if (date != null){

                        val elementInTableUiStateList = date.split("T")
                        val sunriseTimeListWithOffset = elementInTableUiStateList[1].split("-", "+")
                        val sunriseTime = sunriseTimeListWithOffset[0]
                        val day = elementInTableUiStateList[0] //"${dateObject.dayOfMonth}. ${dateObject.month.toString().substring(0, 3)} ${dateObject.year}"
                        val monthInt = day.split("-")[1].toInt()

                        val dateObject: LocalDate = LocalDate.parse(day, DateTimeFormatter.ofPattern(stringResource(R.string.DateFormat)))
                        val dag = "${dateObject.dayOfMonth}. ${dateObject.month.toString().substring(0, 3)} ${dateObject.year}"

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

