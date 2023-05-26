package com.example.sunandmoon.util

import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient

fun fetchLocation(
    fusedLocationClient: FusedLocationProviderClient,
    setCoordinates: (location: Location) -> Unit
) {
    try {


        //Log.i("ararar", "locationAvailability " + fusedLocationClient.locationAvailability.result.toString())
        Log.i("ararar", "fetch location attempt")
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            Log.i("ararar", "fetch location success")
            if (location != null) {
                Log.i("ararar", "lets goo")
                setCoordinates(location)
            } else {

                Log.i("ararar", "Last known location is not available")
                // Handle the case where location is null
                // You can perform additional actions or set default coordinates here
            }
        }
    } catch (e: SecurityException) {
        Log.e("Location", "Location permission is not granted", e)
    } catch (e: Exception) {
        Log.e("Location", "Error fetching last known location: ${e.message}", e)
    }
}


