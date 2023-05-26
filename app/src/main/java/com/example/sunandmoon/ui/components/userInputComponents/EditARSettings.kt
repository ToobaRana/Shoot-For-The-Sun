package com.example.sunandmoon.ui.components.userInputComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.data.ARUIState
import com.example.sunandmoon.model.locationForecastModel.TimePickerColors
import com.example.sunandmoon.viewModel.ARViewModel
import java.time.LocalTime
//used to edit time and date in ar-screen
@Composable
fun EditARSettings(
    modifier: Modifier,
    arUIState: ARUIState,
    arViewModel: ARViewModel = viewModel()
) {
    Column(
        modifier
            .fillMaxSize()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Center
    ) {

        Column(
            modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = modifier.size(20.dp))

            CalendarComponent(modifier,
                arUIState.chosenDateTime,
                updateYear = { year: Int -> arViewModel.updateYear(year) },
                updateMonth = { month: Int, maxDay: Int ->
                    arViewModel.updateMonth(
                        month,
                        maxDay
                    )
                },
                updateDay = { day: Int -> arViewModel.updateDay(day) }
            )

            Spacer(modifier = modifier.size(30.dp))
            Row(modifier = modifier.wrapContentSize()){
                Text(
                    modifier = modifier,
                    text = stringResource(R.string.Time),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                )
                Spacer(modifier = modifier.width(90.dp))
            }


            TimepickerComponent(
                modifier = modifier.wrapContentSize(),
                onValueChange = { time: LocalTime ->
                    arViewModel.updateTime(time)
                },
                enabled = arUIState.editTimeEnabled,
                currentTime = arUIState.chosenDateTime.toLocalTime(),
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

            Spacer(modifier = modifier.size(20.dp))
            Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start){
                Spacer(modifier = modifier.width(20.dp))
                Text(
                    modifier = modifier,
                    text = stringResource(R.string.SunPosition),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                )
            }


            Row(
                modifier
                    .fillMaxWidth(0.95f)
                    .wrapContentSize()
            ) {
                SunPositionTime(
                    modifier = modifier,
                    updateTimePicker = { enabled: Boolean ->
                        arViewModel.timePickerSwitch(
                            enabled
                        )
                    },
                    chosenSunIndex = arUIState.chosenSunPositionIndex,
                    updateChosenIndex = { index: Int ->
                        arViewModel.updateSunPositionIndex(
                            index
                        )
                    }
                )
            }

            Spacer(modifier = modifier.size(20.dp))

            Button(onClick = {
                arViewModel.openCloseARSettings()
            }) {
                Text(text = stringResource(id = R.string.Confirm), fontFamily = FontFamily(Font(R.font.nunito_bold)))
            }

            Spacer(modifier = modifier.size(20.dp))

        }
    }
}