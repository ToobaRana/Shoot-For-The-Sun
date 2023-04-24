package com.example.sunandmoon.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.checkPermissions
import com.example.sunandmoon.ui.components.CalendarComponentDisplay
import com.example.sunandmoon.viewModel.ShootInfoViewModel
import com.google.android.gms.location.FusedLocationProviderClient

//test for fetching last known location
/*@Composable
fun currentLocationTest(
    fusedLocationProviderClient: FusedLocationProviderClient,
    viewModel: ShootInfoViewModel = viewModel(),
    modifier: Modifier
) {
    var showCalendar by remember { mutableStateOf(false)}
    val sunUiState by viewModel.shootInfoUIState.collectAsState()
    //checks permissions to see if button can be enabled
    //put denne før selve funksjonen så den slipper å gjøre konstante permission-kall
    //kan gjøres i viewmodel sin init
    viewModel.updateLocation(checkPermissions())
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        Button(
            onClick = { viewModel.getCurrentPosition(fusedLocationProviderClient) },
            enabled = sunUiState.locationEnabled

        ) {
            Text(text = "Use Current Location")
        }
        if (!sunUiState.locationEnabled) {
            Text("Allow Location to use this", fontSize = 10.sp)
        }

        Text("current location is\n latitude: ${sunUiState.latitude}, \nlongitude:${sunUiState.longitude}")
        var showCalendar by remember { mutableStateOf(false) }
        Button(onClick = {showCalendar = !showCalendar}){
            Text(text = "Show Calendar")
        }
        if (showCalendar){
            CalendarComponentDisplay(modifier, viewModel)
        }
        Text(text = sunUiState.chosenDate.toString())
        //Text(text = "month ${calendar.months[sunUiState.currentMonth]}")

    }
}*/