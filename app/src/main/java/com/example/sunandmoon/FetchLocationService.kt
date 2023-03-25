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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.viewModel.SunViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LastLocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.ktor.client.request.*

import kotlinx.coroutines.delay


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
suspend fun fetchLocation(fusedLocationClient: FusedLocationProviderClient): Pair<Double, Double>? {
    var location :Pair<Double, Double>? = null
    val request = LastLocationRequest.Builder().build()
    try {
        val locationResult = fusedLocationClient.getLastLocation(request)
        delay(100);
        location = Pair(locationResult.result.latitude, locationResult.result.longitude)
    } catch (e: java.lang.IllegalStateException){
        println("location could not be fetched in time")
    }


        // handle exceptions

    return location


}



