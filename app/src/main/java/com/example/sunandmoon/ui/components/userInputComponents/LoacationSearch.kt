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
import com.example.sunandmoon.model.LocationSearchResultsModel.LocationSearchResults
import com.example.sunandmoon.viewModel.CreateShootViewModel
import com.example.sunandmoon.viewModel.ShootInfoViewModel

//https://nominatim.openstreetmap.org/ui/search.html

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearch(
    modifier: Modifier,
    locationSearchQuery: String,
    locationSearchResults: List<LocationSearchResults>,
    setLocationSearchQuery: (query: String) -> Unit,
    loadLocationSearchResults: (query: String) -> Unit,
    setCoordinates: (latitude: Double, longitude: Double, setTimeZoneOffset: Boolean) -> Unit,
) {

    //val shootInfoUIState by createShootViewModel.createShootUIState.collectAsState()

    //var searchResults by remember { mutableStateOf<List<String>>(emptyList()) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = locationSearchQuery,
            onValueChange = { query ->
                setLocationSearchQuery(query)
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
                    if (locationSearchQuery.isNotEmpty()) {
                        loadLocationSearchResults(locationSearchQuery)
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
            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false },
                modifier = modifier.width(IntrinsicSize.Max)
            ) {
                locationSearchResults.forEachIndexed { index, item ->
                    Text(
                        text = item.display_name,
                        modifier = modifier
                            .fillMaxWidth()
                            .clickable {
                                isDropdownExpanded = false
                                setCoordinates(item.lat.toDouble(), item.lon.toDouble(), true)
                            }
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                    if (index < locationSearchResults.lastIndex) {
                        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                    }
                }
            }
        }
    }
}