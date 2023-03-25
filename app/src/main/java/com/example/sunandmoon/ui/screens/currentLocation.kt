package com.example.sunandmoon.ui.screens

import android.app.Activity
import android.content.Context
import android.webkit.WebSettings.TextSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.checkPermissions
import com.example.sunandmoon.viewModel.SunViewModel
import com.google.android.gms.location.FusedLocationProviderClient

@Composable
fun currentLocationTest(fusedLocationProviderClient: FusedLocationProviderClient, viewModel: SunViewModel =viewModel()){
    val hasPermission = checkPermissions()
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        Button(onClick = { viewModel.getCurrentPosition(fusedLocationProviderClient) },
            enabled = hasPermission

        ) {
            Text(text = "Use Current Location")
        }
        if (!hasPermission){
            Text("Allow Location to use this", fontSize = 10.sp)
        }
    }
}