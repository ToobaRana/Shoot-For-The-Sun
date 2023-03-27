package com.example.sunandmoon.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.viewModel.SunViewModel

//https://nominatim.openstreetmap.org/ui/search.html

@OptIn(ExperimentalMaterial3Api::class)
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