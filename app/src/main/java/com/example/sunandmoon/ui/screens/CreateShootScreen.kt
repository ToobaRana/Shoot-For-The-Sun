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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
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
import com.example.sunandmoon.CheckPermissions
import com.example.sunandmoon.model.locationForecastModel.TimePickerColors
import com.example.sunandmoon.ui.components.buttonComponents.GoBackButton
import com.example.sunandmoon.ui.components.userInputComponents.*
import com.example.sunandmoon.util.isNetworkAvailable
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
    CheckPermissions { enabled: Boolean -> createShootViewModel.updateLocation(enabled) }

    if (createShootUIState.parentProductionId == null && parentProductionId != null) {
        createShootViewModel.setParentProductionId(parentProductionId)
    }
    if (createShootUIState.currentShootBeingEditedId == null && shootToEditId != null) {
        createShootViewModel.setCurrentShootBeingEditedId(shootToEditId)
    } else if(shootToEditId == null && !createShootUIState.hasGottenCurrentPosition) {
        createShootViewModel.getCurrentPosition()
    }

    val networkIsAvailable = isNetworkAvailable(context = LocalContext.current)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
                 GoBackButton(modifier, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary, navigateBack)
        }
    ) { innerPadding ->
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


                    //Component for choosing the shoot title
                    OutlinedTextField(
                        value = createShootUIState.name,
                        label = {
                            Text(
                                "Title",
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.nunito_bold))
                            )
                        },
                        onValueChange = { name ->
                            createShootViewModel.updateShootName(name)
                        },
                        placeholder = { Text(stringResource(id = R.string.MyShoot)) },
                        modifier = modifier
                            .fillMaxWidth(0.8f)
                            .padding(5.dp, top = 20.dp),
                        leadingIcon = {
                            Icon(
                                painterResource(R.drawable.edit_icon),
                                stringResource(id = R.string.EditTextPencilIcon),
                                modifier,
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

                if (networkIsAvailable) {
                    LocationSearch(
                        modifier.padding(5.dp),
                        createShootUIState.locationSearchQuery,
                        createShootUIState.locationSearchResults,
                        { query: String, format: Boolean ->
                            createShootViewModel.setLocationSearchQuery(
                                query,
                                format
                            )
                        },
                        { query: String -> createShootViewModel.loadLocationSearchResults(query) },
                        { location: Location ->
                            createShootViewModel.setCoordinates(
                                location
                            )
                        },
                    )
                } else {
                    LatitudeLongitudeInput(
                        modifier,
                        createShootUIState.location,
                        { location: Location -> createShootViewModel.setCoordinates(location) }
                    )
                }
            }

            if (networkIsAvailable) {
                item {
                    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Button(
                            onClick = {

                                createShootViewModel.getCurrentPosition()
                            },
                            enabled = createShootUIState.locationEnabled

                        ) {
                            Text(
                                stringResource(R.string.UseCurrentLocation),
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.nunito_bold))
                            )
                        }
                    }
                    Spacer(modifier = modifier.size(20.dp))
                }
            }

            item {

                CalendarComponent(modifier,
                    createShootUIState.chosenDateTime,
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


                Row(modifier = modifier.wrapContentSize()) {

                    Text(
                        modifier = modifier,
                        text = stringResource(R.string.Time),
                        fontFamily = FontFamily(Font(R.font.nunito_bold)),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = modifier.width(90.dp))
                }


                Row(
                    modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    TimepickerComponent(
                        modifier = modifier.wrapContentSize(),
                        onValueChange = { time: LocalTime ->
                            createShootViewModel.updateTime(time)
                        },
                        enabled = createShootUIState.editTimeEnabled,
                        currentTime = createShootUIState.chosenDateTime.toLocalTime(),
                        colors = TimePickerColors(
                            //cursorColor = MaterialTheme.colorScheme.primary,
                            textColor = MaterialTheme.colorScheme.onSurface,
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                            placeholderColor = MaterialTheme.colorScheme.onSurface,
                            disabledColor = MaterialTheme.colorScheme.secondary,
                        ),
                        fieldShape = RectangleShape,
                        containerShape = RoundedCornerShape(10.dp)
                    )

                }
            }


            item {
                Spacer(modifier = modifier.size(20.dp))
                Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Spacer(modifier = modifier.width(20.dp))
                    Text(
                        modifier = modifier,
                        text = stringResource(R.string.SunPosition),
                        fontSize = 19.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily(Font(R.font.nunito_bold))

                    )
                }

            }
            item {
                Row(
                    modifier
                        .fillMaxWidth(0.9f)
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
                Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Spacer(modifier = modifier.width(20.dp))
                    Text(
                        modifier = modifier,
                        text = stringResource(R.string.PreferredWeather),
                        fontSize = 19.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily(Font(R.font.nunito_bold))


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
                Button(
                    modifier = modifier
                        .padding(30.dp)
                        .width(200.dp)
                        .height(50.dp),
                    onClick = {
                        //save stuff
                        createShootViewModel.saveShoot()
                        navigateBack()
                    }) {
                    Text(
                        text = stringResource(id = R.string.SaveButton),
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_bold))
                    )
                }
            }
        }
    }
}