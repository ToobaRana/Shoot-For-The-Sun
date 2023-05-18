package com.example.sunandmoon.ui.components.userInputComponents


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.sunandmoon.model.LocationForecastModel.TimePickerColors
import java.time.LocalTime
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimepickerComponent(
    modifier: Modifier,
    onValueChange: (chosenTime: LocalTime) -> Unit,
    currentTime: LocalTime,
    colors: TimePickerColors,
    fieldShape: Shape
    //cursorColor = MaterialTheme.colorScheme.primary,

) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(colors.containerColor),
        shape = fieldShape
    )
    {
        Row(modifier = modifier.wrapContentSize()) {

            val hourText =
                if (currentTime.hour != 0) {
                    currentTime.hour.toString()

                } else {
                    ""
                }
            InputField(
                modifier = modifier,
                displayText = hourText,
                onValueChange = { hour: LocalTime -> onValueChange(hour) },
                colors = colors,
                shape = fieldShape,
                min = 0,
                max = 23,
                updateTime = { hour: Int -> currentTime.withHour(hour) }
            )
            val minuteText =
                if (currentTime.minute != 0) {
                    currentTime.minute.toString()

                } else {
                    ""
                }
            InputField(
                modifier = modifier,
                displayText = minuteText,
                onValueChange = { minute: LocalTime -> onValueChange(minute) },
                shape = fieldShape,
                colors = colors,
                min = 0,
                max = 59,
                updateTime = { minute: Int -> currentTime.withMinute(minute) }
            )


        }


    }


}

//checks if number is within range an
fun validateNumber(value: String, min: Int, max: Int): Int? {
    if (value == "") return 0
    if (value.isDigitsOnly()) {
        return value.toInt().coerceIn(min, max)
    }
    return null

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier,
    displayText: String,
    onValueChange: (chosenTime: LocalTime) -> Unit,
    colors: TimePickerColors,
    shape: Shape,
    min: Int,
    max: Int,
    updateTime: (chosenTime: Int) -> LocalTime,

    ) {
    TextField(
        modifier = modifier
            .size(70.dp)
            .padding(2.dp),
        value = displayText,
        onValueChange = { time: String ->
            val newTimeUnit = validateNumber(time, min, max)
            if (newTimeUnit != null) {
                val newTime = updateTime(newTimeUnit)
                onValueChange(newTime)
            }
        },
        placeholder = { Text("00") },
        colors = TextFieldDefaults.textFieldColors(
            textColor = colors.textColor,
            containerColor = colors.containerColor,
            unfocusedIndicatorColor = colors.unfocusedIndicatorColor,
            unfocusedLabelColor = colors.unfocusedLabelColor,
            placeholderColor = colors.placeholderColor,
            )
    )
}



