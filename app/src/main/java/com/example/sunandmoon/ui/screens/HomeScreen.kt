package com.example.sunandmoon.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.ui.components.SunCard
import com.example.sunandmoon.viewModel.SunViewModel
import com.example.sunandmoon.R



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(sunViewModel: SunViewModel = viewModel()){

    val sunUiState by sunViewModel.sunUiState.collectAsState()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
        },

        content = {innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )

            {

                item {
                    SunCard("Sunrise", painterResource(id = R.drawable.sunrise), "05:31")
                    SunCard("Solar noon", painterResource(id = R.drawable.solarnoon), "13:43")
                    SunCard("Sunset", painterResource(id = R.drawable.sunset), "19:51")

                }
            }

        },

        bottomBar = {}
    )
}






