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
fun checkPermissions(setPermission: (permissions: Boolean)->Unit) {
    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { value ->
        setPermission(value)
    }
    val granted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    if (granted == PackageManager.PERMISSION_GRANTED) {
        Log.v("location","Location enabled");
        setPermission(true)
        return
    } else {
        println(granted)
        SideEffect {
            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }
    setPermission(false)

}

//send a request to use last know location, waits 200 ms, then returns it








