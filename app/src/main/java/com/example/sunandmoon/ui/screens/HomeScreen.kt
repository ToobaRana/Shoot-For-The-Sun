package com.example.sunandmoon.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.ui.components.SunCard
import com.example.sunandmoon.viewModel.SunViewModel
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*

import com.example.sunandmoon.R



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier, navigateToNext: () -> Unit, sunViewModel: SunViewModel = viewModel()){


    val sunUiState by sunViewModel.sunUiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {

            LocationSearch()

        },


        content = {innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 20.dp)
            )

            {

                item {
                    SunCard("Sunrise", painterResource(id = R.drawable.sunrise), "05:31")
                    SunCard("Solar noon", painterResource(id = R.drawable.solarnoon), "13:43")
                    SunCard("Sunset", painterResource(id = R.drawable.sunset), "19:51")

                }
            }





        },
        bottomBar = {

            var selectedItem by remember { mutableStateOf(0) }
            val items = listOf("Home", "Table")
            val icons = listOf(Icons.Filled.Star, Icons.Filled.Menu)

            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            if (index == 1){navigateToNext()}

                        }
                    )
                }
            }



        }
    )

}