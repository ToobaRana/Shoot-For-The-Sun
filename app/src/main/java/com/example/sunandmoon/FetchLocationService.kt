package com.example.sunandmoon

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LastLocationRequest
import io.ktor.client.request.*


//checks if app has permissions to use location.

@Composable
fun checkPermissions(): Boolean {

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

    return false

}

//send a request to use last know location, waits 200 ms, then returns it
@SuppressLint("MissingPermission")
fun fetchLocation(
    fusedLocationClient: FusedLocationProviderClient,
    setCoordinates: (latitude: Double, longitude: Double, setTimeZoneOffset: Boolean) -> Unit
) {


    val request = LastLocationRequest.Builder().build()
    try {
        fusedLocationClient.getLastLocation(request).addOnSuccessListener {
            setCoordinates(it.latitude, it.longitude, true);
        }
    } catch (e: java.lang.IllegalStateException) {
        println("location could not be fetched in time")
    }

}







