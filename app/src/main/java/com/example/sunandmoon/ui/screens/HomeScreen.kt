package com.example.sunandmoon.ui.screens

import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.ui.components.SunCard
import com.example.sunandmoon.viewModel.SunViewModel
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*

import com.example.sunandmoon.R
import com.example.sunandmoon.ui.components.CalendarComponent
import com.example.sunandmoon.ui.components.NavigationComposable
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier, navigateToNext: (localDateTime: LocalDateTime, location: Location) -> Unit, sunViewModel: SunViewModel = viewModel()){

    val sunUiState by sunViewModel.sunUiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column() {
                LocationSearch()
                CalendarComponent(modifier)
            }
        },


        content = {innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 20.dp)
            )

            {

                item {
                    SunCard("Sunrise", painterResource(id = R.drawable.sunrise), sunUiState.sunriseTime)
                    SunCard("Solar noon", painterResource(id = R.drawable.solarnoon), sunUiState.solarNoonTime)
                    SunCard("Sunset", painterResource(id = R.drawable.sunset), sunUiState.sunsetTime)
                }
            }

        },
        bottomBar = {
            NavigationComposable(page = 0, navigateToNext)
        }
    )

}