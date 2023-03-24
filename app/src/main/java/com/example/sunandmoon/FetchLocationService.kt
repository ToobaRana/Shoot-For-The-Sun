package com.example.sunandmoon

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.ktor.client.request.*
import kotlin.coroutines.coroutineContext


class FetchLocationService() {
    @Composable
    fun checkPermissions(fusedLocationProviderClient: FusedLocationProviderClient) {

        val context = LocalContext.current
        val isPermissionGranted = remember { mutableStateOf(false) }
        val requestPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            isPermissionGranted.value = isGranted
            Log.v("test", "$isPermissionGranted")

        }
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (granted == PackageManager.PERMISSION_GRANTED) {
            println("JADA");
        } else {
            println(granted)
            SideEffect {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
        }

    }

    @Composable
    fun fetchLocation(fusedLocationProviderClient: FusedLocationProviderClient) {
        val result = remember { mutableStateOf<Bitmap?>(null) }

/*
    var location by remember{ mutableStateOf<Pair<Double, Double>?>(null)}

    val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(LocalContext.current)



        try {
            val locationResult = fusedLocationClient.lastLocation.await()
            location = Pair(locationResult.latitude, locationResult.longitude)
        } catch (e: Exception) {
            // handle exceptions
        }
    }

    if (location != null) {
        Text("Latitude: ${location!!.first}, Longitude: ${location!!.second}")
    } else {
        Text("Fetching location...")
    }
}*/
    }
}

