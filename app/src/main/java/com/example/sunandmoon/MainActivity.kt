package com.example.sunandmoon

import android.content.Context
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
import androidx.room.Room
import com.example.sunandmoon.data.localDatabase.AppDatabase
import com.example.sunandmoon.data.localDatabase.dao.ProductionDao
import com.example.sunandmoon.data.localDatabase.dao.ShootDao
import com.example.sunandmoon.data.util.LocationAndDateTime
import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.ui.screens.CreateShootScreen
import com.example.sunandmoon.ui.screens.ProductionSelectionScreen
import com.example.sunandmoon.ui.screens.ShootInfoScreen
import com.example.sunandmoon.ui.screens.TableScreen
import com.example.sunandmoon.ui.theme.SunAndMoonTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
//import com.example.sunandmoon.di.DaggerAppComponent



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    //initializing here to get context of activity (this) before setcontent
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val modifier = Modifier

        setContent {
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
        val routeProductionSelectionScreen = "productionSelectionScreen"
        composable(routeProductionSelectionScreen) {
            ProductionSelectionScreen(
                modifier = modifier,
                navigateToShootInfoScreen = { shootId: Int -> navController.navigate("shootInfoScreen/$shootId")},
                navigateToNextBottomBar = { index: Int ->
                    when (index) {
                        0 -> navController.popBackStack("productionSelectionScreen", false)
                        1 -> navController.navigate("tableScreen")
                        2 -> navController.navigate("tableScreen")
                    }
                },
                navigateToCreateShootScreen = { parentProductionId: Int?, shootToEditId: Int? -> navController.navigate("createShootScreen/$parentProductionId/$shootToEditId") },
                navController = navController,
                currentScreenRoute = routeProductionSelectionScreen
            )
        }
        val routeShootInfoScreen = "shootInfoScreen/{shootId}"
        composable(routeShootInfoScreen) { backStackEntry ->
            val shootId: Int? = backStackEntry.arguments?.getString("shootId")?.toIntOrNull()
            if(shootId != null) {
                ShootInfoScreen(
                    modifier = modifier,
                    navigateBack = { navController.popBackStack("productionSelectionScreen", false) },
                    shootId = shootId,
                    navigateToCreateShootScreen = { parentProductionId: Int?, shootToEditId: Int? -> navController.navigate("createShootScreen/$parentProductionId/$shootToEditId") },
                    navController = navController,
                    currentScreenRoute = routeShootInfoScreen
                )
            }
        }
        composable("createShootScreen/{parentProductionId}/{shootToEditId}") { backStackEntry ->
            val parentProductionId: Int? = backStackEntry.arguments?.getString("parentProductionId")?.toIntOrNull()
            val shootToEditId: Int? = backStackEntry.arguments?.getString("shootToEditId")?.toIntOrNull()
            val routeToGoBackTo: String = if(shootToEditId != null) routeShootInfoScreen else routeProductionSelectionScreen

            CreateShootScreen(
                modifier = modifier,
                navigateBack = { navController.popBackStack(routeToGoBackTo, false) },
                parentProductionId = parentProductionId,
                shootToEditId = shootToEditId
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

    val shootName: String = backStackEntry.arguments?.getString("shootName")!!
    val locationName: String = backStackEntry.arguments?.getString("locationName")!!

    val timeZoneOffset: Double = backStackEntry.arguments?.getString("timeZoneOffset")!!.toDouble()

    val shootId: Int = backStackEntry.arguments?.getString("shootId")!!.toInt()

    return Shoot(name = shootName, locationName = locationName, location = location, date = localDateTime, timeZoneOffset = timeZoneOffset, id = shootId)
}