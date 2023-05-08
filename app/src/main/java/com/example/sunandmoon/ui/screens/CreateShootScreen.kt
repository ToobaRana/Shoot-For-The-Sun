package com.example.sunandmoon.ui.screens

import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.ui.components.CalendarComponent
import com.example.sunandmoon.ui.components.NavigationComposable
import com.example.sunandmoon.viewModel.CreateShootViewModel
import com.example.sunandmoon.viewModel.ShootInfoViewModel
import java.time.LocalDateTime



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateShootScreen(modifier: Modifier, navigateToNext: () -> Unit, createShootViewModel: CreateShootViewModel = viewModel()){

    val createShootUIState by createShootViewModel.createShootUIState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),

        content = {innerPadding ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 20.dp)
            )

            {
                item {
                    LocationSearch(
                        modifier,
                        createShootUIState.locationSearchQuery,
                        createShootUIState.locationSearchResults,
                        {query: String -> createShootViewModel.setLocationSearchQuery(query)},
                        {query: String -> createShootViewModel.loadLocationSearchResults(query)},
                        {latitude: Double, longitude: Double, setTimeZoneOffset: Boolean -> createShootViewModel.setCoordinates(latitude, longitude, setTimeZoneOffset)},
                    )
                    CalendarComponent(modifier)

                    Button(onClick = {
                        navigateToNext()
                    }) {
                        Text(text = "Save")
                    }
                }
            }
        }
    )
}