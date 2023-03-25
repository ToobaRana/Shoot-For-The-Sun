package com.example.sunandmoon

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.ktor.client.request.*



@Composable
    fun checkPermissions(): Boolean{

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
            println("JADA!! VI FANT DEG");
            return true

        } else {
            println(granted)
            SideEffect {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
        }
        if (granted == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false

    }


    @SuppressLint("MissingPermission")

fun fetchLocation(fusedLocationClient: FusedLocationProviderClient) {
    var location :Pair<Double, Double>? = null

    try {
        val locationResult = fusedLocationClient.lastLocation.result
        location = Pair(locationResult.latitude, locationResult.longitude)
    } catch (e: Exception) {
        // handle exceptions
    }


    if (location != null) {
       Log.v("WTF"  ,"${location!!.first}, Longitude: ${location!!.second}"    )
    } else {
         Log.v("WTF","Fetching location..."       )
    }
}



