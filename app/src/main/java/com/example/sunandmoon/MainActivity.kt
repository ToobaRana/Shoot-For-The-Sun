package com.example.sunandmoon

import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sunandmoon.data.util.LocationAndDateTime
import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.ui.screens.ProductionSelectionScreen
import com.example.sunandmoon.ui.screens.ShootInfoScreen
import com.example.sunandmoon.ui.screens.TableScreen
import com.example.sunandmoon.ui.theme.SunAndMoonTheme
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
            val modifier = Modifier
            SunAndMoonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = modifier,
                    color = MaterialTheme.colorScheme.background
                ) {
                    MultipleScreenNavigator(modifier)
                }
            }
        }

    }
}

@Composable
fun MultipleScreenNavigator(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "productionSelectionScreen") {
        composable("productionSelectionScreen") {
            ProductionSelectionScreen(
                modifier = modifier,
                navigateToShootInfoScreen = { shoot: Shoot -> navController.navigate("shootInfoScreen/${shoot.name}/${shoot.locationName}/${shoot.date}/${shoot.location.latitude}/${shoot.location.longitude}/${shoot.timeZoneOffset}")},
                navigateToNextBottomBar = { index: Int ->
                    when (index) {
                        0 -> navController.popBackStack("productionSelectionScreen", false)
                        1 -> navController.navigate("tableScreen")
                        2 -> navController.navigate("tableScreen")
                    }
                }
            )
        }
        composable("shootInfoScreen/{shootName}/{locationName}/{localDateTime}/{latitude}/{longitude}/{timeZoneOffset}") { backStackEntry ->
            ShootInfoScreen(
                modifier = modifier,
                navigateToNext = { navController.navigate("tableScreen")},
                shoot = getShootFromArgs(backStackEntry)
            )
        }
        composable("tableScreen"){ backStackEntry ->
            TableScreen(
                modifier = modifier,
                navigateToNextBottomBar = { index: Int ->
                    when (index) {
                        0 -> navController.popBackStack("productionSelectionScreen", false)
                        1 -> navController.navigate("tableScreen")
                        2 -> navController.navigate("tableScreen")
                    }
                }
            )
        }
    }
}

fun getShootFromArgs(backStackEntry: NavBackStackEntry): Shoot {
    var localDateTime: LocalDateTime = LocalDateTime.parse(backStackEntry.arguments?.getString("localDateTime"), DateTimeFormatter.ISO_DATE_TIME)

    var location: Location = Location("provider")
    location.latitude = backStackEntry.arguments?.getString("latitude")?.toDouble() ?: 0.0
    location.longitude = backStackEntry.arguments?.getString("longitude")?.toDouble() ?: 0.0

    return Shoot(name = "test", locationName = "test2", location = location, date = localDateTime, timeZoneOffset = 2.0)
}