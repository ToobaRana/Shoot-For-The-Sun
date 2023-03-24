package com.example.sunandmoon.ui.screens

import android.content.Context
import android.location.Geocoder
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.viewModel.SunViewModel
import java.util.*

//https://nominatim.openstreetmap.org/ui/search.html

@Composable
fun LocationSearch(sunViewModel: SunViewModel = viewModel()) {

    val sunUIState by sunViewModel.sunUiState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    //var searchResults by remember { mutableStateOf<List<String>>(emptyList()) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column() {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
            },
            label = { Text("Search for a location") },
            placeholder = { Text("Enter a location") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // handle search button press
                }
            )
        )

        Button(onClick = {
            if (searchQuery.isNotEmpty()) {
                sunViewModel.loadLocationSearchResults(searchQuery)
                isDropdownExpanded = true
            }
        }) {
            Text(text = "search")
        }

        if (isDropdownExpanded) {
            val searchResults = sunUIState.locationSearchResults
            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false },
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                searchResults.forEach { result ->
                    DropdownMenuItem(onClick = {
                        searchQuery = result.display_name
                        isDropdownExpanded = false

                    }, text = {Text(result.display_name)})
                }
            }
        }
    }
}