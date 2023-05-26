package com.example.sunandmoon.ui.screens

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