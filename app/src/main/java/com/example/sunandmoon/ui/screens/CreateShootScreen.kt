package com.example.sunandmoon.ui.screens


import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.checkPermissions
import com.example.sunandmoon.model.LocationForecastModel.TimePickerColors
import com.example.sunandmoon.ui.components.CalendarComponent
import com.example.sunandmoon.ui.components.userInputComponents.PreferredWeatherComponent
import com.example.sunandmoon.ui.components.userInputComponents.SunPositionTime
import com.example.sunandmoon.ui.components.userInputComponents.TimepickerComponent
import com.example.sunandmoon.viewModel.CreateShootViewModel
import java.time.LocalTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateShootScreen(
    modifier: Modifier,
    navigateBack: () -> Unit,
    createShootViewModel: CreateShootViewModel = hiltViewModel(),
    parentProductionId: Int?,
    shootToEditId: Int?,
) {

    val createShootUIState by createShootViewModel.createShootUIState.collectAsState()
    checkPermissions { enabled: Boolean -> createShootViewModel.updateLocation(enabled) }

    if (createShootUIState.parentProductionId == null && parentProductionId != null) {
        createShootViewModel.setParentProductionId(parentProductionId)
    }
    if (createShootUIState.currentShootBeingEditedId == null && shootToEditId != null) {
        createShootViewModel.setCurrentShootBeingEditedId(shootToEditId)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        content = { innerPadding ->
            val focusManager = LocalFocusManager.current

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )

            {
                item {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {


                        TextField(value = createShootUIState.name,
                            label = { Text("Title", fontSize = 18.sp) },
                            onValueChange = { name ->
                                createShootViewModel.updateShootName(name)
                            },
                            placeholder = { Text("My Shoot") },
                            modifier = modifier.fillMaxWidth(0.9f),
                            leadingIcon = {
                                Icon(
                                    painterResource(R.drawable.pencil),
                                    "Edit text pencil icon",
                                    Modifier,
                                    MaterialTheme.colorScheme.primary
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                //cursorColor = MaterialTheme.colorScheme.primary,
                                textColor = MaterialTheme.colorScheme.onSurface,
                                containerColor = MaterialTheme.colorScheme.background,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
                            ),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            )


                        )

                    }


                }
                item {
                    Spacer(modifier = modifier.size(20.dp))
                    Divider(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(3.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = modifier.size(20.dp))
                }
                item {
                    LocationSearch(
                        modifier,
                        createShootUIState.locationSearchQuery,
                        createShootUIState.locationSearchResults,
                        { query: String -> createShootViewModel.setLocationSearchQuery(query) },
                        { query: String -> createShootViewModel.loadLocationSearchResults(query) },
                        { location: Location, setTimeZoneOffset: Boolean ->
                            createShootViewModel.setCoordinates(
                                location,
                                setTimeZoneOffset
                            )
                        },
                    )
                }

                item {
                    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Button(
                            onClick = {

                                createShootViewModel.getCurrentPosition()
                            },
                            enabled = createShootUIState.locationEnabled

                        ) {
                            Text(stringResource(R.string.UseCurrentLocation), fontSize = 18.sp)
                        }
                    }
                }

                item {

                    CalendarComponent(modifier,
                        createShootUIState.chosenDate,
                        updateYear = { year: Int -> createShootViewModel.updateYear(year) },
                        updateMonth = { month: Int, maxDay: Int ->
                            createShootViewModel.updateMonth(
                                month,
                                maxDay
                            )
                        },
                        updateDay = { day: Int -> createShootViewModel.updateDay(day) })
                }
                item {
                    Spacer(modifier = modifier.size(30.dp))
                    Row(modifier = modifier.wrapContentSize()){
                        Text(
                            modifier = modifier,
                            text = stringResource(R.string.Time),
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = FontFamily(Font(R.font.nunito))
                        )
                        Spacer(modifier = modifier.width(90.dp))
                    }


                    TimepickerComponent(
                        modifier = modifier.wrapContentSize(),
                        onValueChange = { time: LocalTime ->
                            createShootViewModel.updateTime(time)
                        },
                        enabled = createShootUIState.timeEnabled,
                        currentTime = createShootUIState.chosenDate.toLocalTime(),
                        colors = TimePickerColors(
                            //cursorColor = MaterialTheme.colorScheme.primary,
                            textColor = MaterialTheme.colorScheme.onSurface,
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                            placeholderColor = MaterialTheme.colorScheme.onSurface,
                            disabledTextColor = Color.Black
                        ),
                        fieldShape = RectangleShape,
                        containerShape = RoundedCornerShape(10.dp)
                    )
                }
                item {
                    Spacer(modifier = modifier.size(20.dp))
                    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start){
                        Spacer(modifier = modifier.width(20.dp))
                        Text(
                            modifier = modifier,
                            text = stringResource(R.string.SunPosition),
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = FontFamily(Font(R.font.nunito))


                        )
                    }

                }
                item {
                    Row(
                        modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                    ) {
                        SunPositionTime(
                            modifier = modifier,
                            updateTimePicker = { enabled: Boolean ->
                                createShootViewModel.timePickerSwitch(
                                    enabled
                                )
                            },
                            chosenSunIndex = createShootUIState.chosenSunPositionIndex,
                            updateChosenIndex = { index: Int ->
                                createShootViewModel.updateSunPositionIndex(
                                    index
                                )
                            }
                        )
                    }

                }
                item {
                    Spacer(modifier = modifier.size(20.dp))
                    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start){
                        Spacer(modifier = modifier.width(20.dp))
                        Text(
                            modifier = modifier,
                            text = stringResource(R.string.PreferredWeather),
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = FontFamily(Font(R.font.nunito))


                        )
                    }

                }
                item {
                    Row(
                        modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                    ) {
                        PreferredWeatherComponent(
                            modifier = modifier,
                            preferredWeather = createShootUIState.preferredWeather,
                        )
                    }
                }
                item {
                    Button(onClick = {
                        //save stuff
                        createShootViewModel.saveShoot()
                        navigateBack()
                    }) {
                        Text(text = "Save")
                    }
                }
            }
        }
    )
}