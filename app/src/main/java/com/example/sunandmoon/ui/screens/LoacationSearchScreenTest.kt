package com.example.sunandmoon.ui.screens

import android.content.Context
import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
                    if (searchQuery.isNotEmpty()) {
                        sunViewModel.loadLocationSearchResults(searchQuery)
                        isDropdownExpanded = true
                    }
                }
            )
        )

        if (isDropdownExpanded) {
            val searchResults = sunUIState.locationSearchResults
            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false },
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                searchResults.forEachIndexed { index, item ->
                    Text(
                        text = item.display_name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isDropdownExpanded = false
                                // Perform action when item is clicked
                            }
                            .padding(vertical = 8.dp, horizontal = 16.dp)

                    )
                    if (index < searchResults.lastIndex) {
                        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                    }
                }
            }
        }
    }
}