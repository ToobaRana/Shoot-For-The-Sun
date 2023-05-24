package com.example.sunandmoon.ui.screens

import com.example.sunandmoon.ui.components.buttonComponents.GoBackButton
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*

import com.example.sunandmoon.ui.components.infoComponents.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(modifier: Modifier, navigateBack: () -> Unit){

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)) {
                GoBackButton(modifier, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary, navigateBack)
            }
        },


        ) { innerPadding -> modifier.padding(10.dp)
        AboutCard(modifier.padding(innerPadding))
    }

}