package com.example.sunandmoon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.sunandmoon.ui.screens.HomeScreen
import com.example.sunandmoon.ui.screens.currentLocationTest
import com.example.sunandmoon.ui.theme.SunAndMoonTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

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
                    //


                    HomeScreen()

                    
                    //SunViewModel()
                    //MathScreen()
                    //LocationSearch()
                    //currentLocationTest(fusedLocationClient)

                }
            }
        }

    }
}

