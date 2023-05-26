package com.example.sunandmoon.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.example.sunandmoon.viewModel.ShootInfoViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import com.example.sunandmoon.R
import com.example.sunandmoon.ui.components.buttonComponents.GoBackEditDeleteBar
import com.example.sunandmoon.ui.components.infoComponents.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShootInfoScreen(
    modifier: Modifier,
    navigateBack: () -> Unit,
    shootInfoViewModel: ShootInfoViewModel = hiltViewModel(),
    shootId: Int,
    navigateToCreateShootScreen: (parentProductionId: Int?, shootToEditId: Int?) -> Unit,
    navController: NavController,
    currentScreenRoute: String
) {

    val shootInfoUIState by shootInfoViewModel.shootInfoUIState.collectAsState()

    if (shootInfoUIState.shoot == null) {
        shootInfoViewModel.getShoot(shootId)
        return
    }

    val dateAndTime = shootInfoUIState.shoot!!.dateTime
    val date = dateAndTime.toLocalDate()
    val timeWithSeconds = dateAndTime.toLocalTime()
    val time = timeWithSeconds.truncatedTo(ChronoUnit.MINUTES)

    // when you navigate back to this screen we want to get the shoots from the database again
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.route == currentScreenRoute) {
                // get all the shoots from the database when popped back to this screen
                shootInfoViewModel.refreshShoot()
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                GoBackEditDeleteBar(
                    modifier,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary,
                    navigateBack,
                    { navigateToCreateShootScreen(null, shootId) },
                    {
                        shootInfoViewModel.deleteShoot()
                        navigateBack()
                    }
                )

                //Header for shoot name
                Text(
                    text = shootInfoUIState.shoot!!.name,
                    modifier = modifier
                        .fillMaxWidth()
                        .align(CenterHorizontally),
                    fontSize = 50.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    lineHeight = 50.sp
                )

                //Location for shoot
                Row(modifier = modifier.align(CenterHorizontally)) {
                    Icon(
                        painter = painterResource(id = R.drawable.location1),
                        "Location Icon",
                        modifier = modifier
                            .size(35.dp)
                            .padding(end = 5.dp),
                        MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = shootInfoUIState.shoot!!.locationName,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = modifier.size(40.dp))

                //Calendar and time
                Row(modifier = modifier.align(CenterHorizontally)) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        stringResource(id = R.string.CalendarIcon),
                        modifier = modifier
                            .size(35.dp)
                            .padding(end = 5.dp),
                        MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${date.dayOfMonth}. ${
                            date.month.toString().substring(0, 3)
                        } ${date.year}", fontSize = 20.sp, color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = modifier.size(30.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.clock),
                        stringResource(id = R.string.ClockIcon),
                        modifier = modifier
                            .size(35.dp)
                            .padding(end = 5.dp),
                        MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = time.toString(),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

            }
        },


        ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 20.dp)
        )

        {
            item {

                SunPositionsCard(
                    modifier = modifier,
                    sunriseTime = shootInfoUIState.sunriseTime,
                    solarNoonTime = shootInfoUIState.solarNoonTime,
                    sunsetTime = shootInfoUIState.sunsetTime
                )

                //if statement added because weatherapi doesnt have data from before todays data and after 10 days
                if (dateAndTime < LocalDateTime.now()
                        .minusHours(1) || dateAndTime >= LocalDateTime.now().plusDays(10)
                ) {
                    NoDataCard(modifier, shootInfoUIState.missingNetworkConnection)
                } else {
                    if (shootInfoUIState.weatherIcon != null) {
                        WeatherCard(
                            modifier = modifier,
                            time,
                            shootInfoUIState.temperature,
                            shootInfoUIState.rainfallInMm,
                            shootInfoUIState.weatherIcon
                        )
                    }

                    WindCard(
                        modifier = modifier,
                        time,
                        shootInfoUIState.windSpeed,
                        shootInfoUIState.windDirection
                    )

                    UVCard(
                        modifier = modifier, time, shootInfoUIState.uvIndex
                    )
                }


            }
        }

    }

}