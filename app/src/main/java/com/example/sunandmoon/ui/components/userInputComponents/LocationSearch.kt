package com.example.sunandmoon.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.sunandmoon.R
import com.example.sunandmoon.model.LocationSearchResultsModel.LocationSearchResults

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

    val focusManager = LocalFocusManager.current

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
                    focusManager.clearFocus()
                }
            ),
            leadingIcon = {
                Icon(painterResource(R.drawable.searchlocation), "location search field icon", Modifier, MaterialTheme.colorScheme.primary)
            },
            colors = TextFieldDefaults.textFieldColors(
                //cursorColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
            ),
            maxLines = 1
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
                                setLocationSearchQuery(item.display_name.split(",")[0])
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