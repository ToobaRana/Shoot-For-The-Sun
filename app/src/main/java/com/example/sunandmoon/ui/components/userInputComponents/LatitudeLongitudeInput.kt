package com.example.sunandmoon.ui.components.userInputComponents

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R

//used for manual input of latitude and longitude in offline-mode
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatitudeLongitudeInput(
    modifier: Modifier,
    currentLocation: Location,
    setCoordinates: (location: Location) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)) {
        Row(modifier = modifier
            .fillMaxWidth(0.8f)
            .align(Alignment.CenterHorizontally)) {
            OutlinedTextField(
                value = currentLocation.latitude.toString(),
                onValueChange = { query ->
                    try {
                        setCoordinates(currentLocation.apply { latitude = query.toDouble() })
                    } catch (e: Exception) {
                        Log.i("LatitudeLongitudeInput", "Invalid latitude")
                    }
                },
                label = { Text(stringResource(id = R.string.Latitude), fontSize = 18.sp, fontFamily = FontFamily(Font(R.font.nunito_bold))) },
                //placeholder = { Text("Enter location", fontSize = 18.sp) },
                singleLine = true,
                modifier = modifier
                    .weight(1f)
                    .padding(end = 5.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(
                    onSearch = {
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

            OutlinedTextField(
                value = currentLocation.longitude.toString(),
                onValueChange = { query ->
                    try {
                        setCoordinates(currentLocation.apply { longitude = query.toDouble() })
                    } catch (e: Exception) {
                        Log.i("LatitudeLongitudeInput", "Invalid longitude")
                    }
                },
                label = { Text("Longitude", fontSize = 18.sp, fontFamily = FontFamily(Font(R.font.nunito_bold))) },
                //placeholder = { Text("Enter location", fontSize = 18.sp) },
                singleLine = true,
                modifier = modifier
                    .weight(1f)
                    .padding(start = 5.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(
                    onSearch = {
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
        }
    }
}