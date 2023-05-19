package com.example.sunandmoon

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sunandmoon.ar.SunAR
import com.example.sunandmoon.navigation.MultipleScreenNavigator
import com.example.sunandmoon.ui.screens.*
import com.example.sunandmoon.ui.theme.SunAndMoonTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.AndroidEntryPoint

//import com.example.sunandmoon.di.DaggerAppComponent



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    //initializing here to get context of activity (this) before setcontent
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val modifier = Modifier // this instance of the modifier class is passed down to all our other composables

        setContent {
            SunAndMoonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = modifier,
                    color = MaterialTheme.colorScheme.background
                ) {
                    MultipleScreenNavigator(modifier, packageManager)
                }
            }
        }
    }
}

fun bottomBarNavigation(index: Int, navController: NavController) {
    when (index) {
        0 -> navController.popBackStack("productionSelectionScreen", false)
        1 -> navController.navigate("ARScreen")
        2 -> navController.navigate("tableScreen")
    }
}