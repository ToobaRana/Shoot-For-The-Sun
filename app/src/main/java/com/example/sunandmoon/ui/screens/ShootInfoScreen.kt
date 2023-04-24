package com.example.sunandmoon.ui.screens

import android.location.Location
import androidx.compose.foundation.clickable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.viewModel.ShootInfoViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import com.example.sunandmoon.R
import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.ui.components.CalendarComponent
import com.example.sunandmoon.ui.components.NavigationComposable
import com.example.sunandmoon.ui.components.infoComponents.SunPositionsCard
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShootInfoScreen(modifier: Modifier, navigateToNext: () -> Unit, shootInfoViewModel: ShootInfoViewModel = viewModel(), shoot: Shoot){

    val shootInfoUIState by shootInfoViewModel.shootInfoUIState.collectAsState()

    if (shootInfoUIState.shoot == null) {
        shootInfoViewModel.setShoot(shoot)
        return
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(modifier.fillMaxWidth()) {
                TextField(
                    value = shootInfoUIState.shoot!!.name,
                    onValueChange = { query ->
                    },
                    label = { Text("Name of your shoot") },
                    singleLine = true,
                    modifier = modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.CenterHorizontally),
                    leadingIcon = {
                        Icon(painterResource(R.drawable.find_shoot_icon), "location search field icon", Modifier, MaterialTheme.colorScheme.primary)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        //cursorColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.onSurface,
                        containerColor = MaterialTheme.colorScheme.background,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                TextField(
                    value = shootInfoUIState.shoot!!.locationName,
                    onValueChange = { query ->
                    },
                    label = { Text("Name of your shoot") },
                    singleLine = true,
                    modifier = modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.CenterHorizontally),
                    leadingIcon = {
                        Icon(painterResource(R.drawable.find_shoot_icon), "location search field icon", Modifier, MaterialTheme.colorScheme.primary)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        //cursorColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.onSurface,
                        containerColor = MaterialTheme.colorScheme.background,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        },


        content = {innerPadding ->
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
                        solarNoonTime = shootInfoUIState.solarNoonTime ,
                        sunsetTime = shootInfoUIState.sunsetTime
                    )



                }
            }

        }
    )

}