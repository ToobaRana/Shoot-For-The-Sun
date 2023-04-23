package com.example.sunandmoon.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.viewModel.ShootInfoViewModel

//https://nominatim.openstreetmap.org/ui/search.html

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearch(shootInfoViewModel: ShootInfoViewModel = viewModel(), modifier: Modifier) {

    val shootInfoUIState by shootInfoViewModel.shootInfoUIState.collectAsState()

    var searchQuery = shootInfoUIState.locationSearchQuery

    //var searchResults by remember { mutableStateOf<List<String>>(emptyList()) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = searchQuery,
            onValueChange = { query ->
                shootInfoViewModel.setLocationSearchQuery(query)
            },
            label = { Text("Search for a location") },
            placeholder = { Text("Enter a location") },
            singleLine = true,
            modifier = modifier.fillMaxWidth(0.8f).align(Alignment.CenterHorizontally),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // handle search button press
                    if (searchQuery.isNotEmpty()) {
                        shootInfoViewModel.loadLocationSearchResults(searchQuery)
                        isDropdownExpanded = true
                    }
                }
            ),
            leadingIcon = {
                Icon(painterResource(R.drawable.find_shoot_icon), "location search field icon", Modifier, MaterialTheme.colorScheme.primary)
            },
            colors = TextFieldDefaults.textFieldColors(
                //cursorColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
            )
        )

        if (isDropdownExpanded) {
            val searchResults = shootInfoUIState.locationSearchResults
            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false },
                modifier = modifier.width(IntrinsicSize.Max)
            ) {
                searchResults.forEachIndexed { index, item ->
                    Text(
                        text = item.display_name,
                        modifier = modifier
                            .fillMaxWidth()
                            .clickable {
                                isDropdownExpanded = false
                                shootInfoViewModel.setCoordinates(item.lat.toDouble(), item.lon.toDouble(), true)
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