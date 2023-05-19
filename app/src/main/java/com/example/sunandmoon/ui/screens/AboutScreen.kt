package com.example.sunandmoon.ui.screens

import com.example.sunandmoon.ui.components.buttonComponents.GoBackButton
import android.location.Location
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import com.example.sunandmoon.R
import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.model.LocationForecastModel.Timeseries
import com.example.sunandmoon.ui.components.CalendarComponent
import com.example.sunandmoon.ui.components.NavigationComposable
import com.example.sunandmoon.ui.components.buttonComponents.GoBackEditDeleteBar
import com.example.sunandmoon.ui.components.infoComponents.*
import com.example.sunandmoon.ui.theme.UVLowColor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(modifier: Modifier, navigateBack: () -> Unit){

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)) {
                GoBackButton(modifier, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary, navigateBack)
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

            }
        }

    }

}