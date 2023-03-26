package com.example.sunandmoon

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LastLocationRequest
import io.ktor.client.request.*
import kotlinx.coroutines.delay


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
    if (granted == PackageManager.PERMISSION_GRANTED) {
        return true
    }
    return false

}

//send a request to use last know location, waits 200 ms, then returns it
@SuppressLint("MissingPermission")
suspend fun fetchLocation(fusedLocationClient: FusedLocationProviderClient): Pair<Double, Double>? {
    var location: Pair<Double, Double>? = null
    val request = LastLocationRequest.Builder().build()
    try {
        val locationResult = fusedLocationClient.getLastLocation(request)
        delay(200);
        location = Pair(locationResult.result.latitude, locationResult.result.longitude)
    } catch (e: java.lang.IllegalStateException) {
        println("location could not be fetched in time")
    }


    // handle exceptions

    return location


}



