package com.example.sunandmoon

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sunandmoon.data.util.LocationAndDateTime
import com.example.sunandmoon.ui.screens.HomeScreen
import com.example.sunandmoon.ui.screens.TableScreen
import com.example.sunandmoon.ui.screens.TableView
import com.example.sunandmoon.ui.theme.SunAndMoonTheme
import com.example.sunandmoon.viewModel.SunViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    //initializing here to get context of activity (this) before setcontent
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {

            SunAndMoonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.background
                ) {
                    MultipleScreenNavigator()
                }
            }
        }

    }
}

@Composable
fun MultipleScreenNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "homescreen/{localDateTime}/{latitude}/{longitude}") {
        composable("homescreen/{localDateTime}/{latitude}/{longitude}") {
            HomeScreen(
                modifier = Modifier,
                navigateToNext = { localDateTime: LocalDateTime, location: Location -> navController.navigate("tablescreen/${localDateTime}/${location.latitude}/${location.longitude}")}
            )
        }
        composable("tablescreen/{localDateTime}/{latitude}/{longitude}"){ backStackEntry ->
            TableScreen(
                modifier = Modifier,
                navigateToNext = {localDateTime: LocalDateTime, location: Location -> navController.popBackStack("homescreen/{localDateTime}/{latitude}/{longitude}", false) },
                locationAndDateTime = readArgsAndGetDataToTransfer(backStackEntry)
            )
        }
    }
}

fun readArgsAndGetDataToTransfer(backStackEntry: NavBackStackEntry): LocationAndDateTime {
    var localDateTime: LocalDateTime = LocalDateTime.parse(backStackEntry.arguments?.getString("localDateTime"), DateTimeFormatter.ISO_DATE_TIME)

    var location: Location = Location("provider")
    location.latitude = backStackEntry.arguments?.getString("latitude")?.toDouble() ?: 0.0
    location.longitude = backStackEntry.arguments?.getString("longitude")?.toDouble() ?: 0.0

    return LocationAndDateTime(location, localDateTime)
}