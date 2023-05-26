package com.example.sunandmoon.ui.components.userInputComponents

import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R
import com.example.sunandmoon.model.locationSearchResultsModel.LocationSearchResults

//https://nominatim.openstreetmap.org/ui/search.html
//used to search in location-api
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearch(
    modifier: Modifier,
    locationSearchQuery: String,
    locationSearchResults: List<LocationSearchResults>,
    setLocationSearchQuery: (query: String, format: Boolean) -> Unit,
    loadLocationSearchResults: (query: String) -> Unit,
    setCoordinates: (location: Location) -> Unit,
) {

    //val shootInfoUIState by createShootViewModel.createShootUIState.collectAsState()

    //var searchResults by remember { mutableStateOf<List<String>>(emptyList()) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = locationSearchQuery,
            onValueChange = { query ->
                setLocationSearchQuery(query, false)
            },
            label = { Text(stringResource(id = R.string.SearchLocation), fontSize = 18.sp, fontFamily = FontFamily(Font(R.font.nunito_bold))) },
            placeholder = { Text(stringResource(id = R.string.EnterLocation), fontSize = 18.sp) },
            singleLine = true,
            modifier = modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally),
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
                Icon(painterResource(R.drawable.searchlocation), stringResource(id = R.string.LocationSearchFieldIcon), Modifier, MaterialTheme.colorScheme.primary)
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
                expanded = true,
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
                                setCoordinates(Location("").apply {
                                    latitude = item.lat.toDouble(); longitude = item.lon.toDouble()
                                })
                                setLocationSearchQuery(item.display_name, true)
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