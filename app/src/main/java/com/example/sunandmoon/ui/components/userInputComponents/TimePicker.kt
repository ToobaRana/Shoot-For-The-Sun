package com.example.sunandmoon.ui.components.userInputComponents


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.sunandmoon.model.LocationForecastModel.TimePickerColors
import java.time.LocalTime

@Composable
fun TimepickerComponent(
    modifier: Modifier,
    onValueChange: (chosenTime: LocalTime) -> Unit,
    currentTime: LocalTime,
    colors: TimePickerColors,
    fieldShape: Shape,
    containerShape: Shape
    //cursorColor = MaterialTheme.colorScheme.primary,

) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(colors.containerColor),
        shape = containerShape,
        border = BorderStroke(2.dp, colors.textColor)
    )
    {
        Row(
            modifier = modifier.size(width = 150.dp, height = 75.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val hourText =
                if (currentTime.hour != 0) {
                    currentTime.hour.toString()

                } else {
                    ""
                }
            InputField(
                modifier = modifier.align(Alignment.CenterVertically),
                displayText = hourText,
                onValueChange = { hour: LocalTime -> onValueChange(hour) },
                colors = colors,
                shape = fieldShape,
                min = 0,
                max = 23,
                updateTime = { hour: Int -> currentTime.withHour(hour) }
            )
            Text(modifier = modifier.weight(1f), text = ":")
            val minuteText =
                if (currentTime.minute != 0) {
                    currentTime.minute.toString()

                } else {
                    ""
                }
            InputField(
                modifier = modifier.align(Alignment.CenterVertically),
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
    val focusManager = LocalFocusManager.current
    TextField(
        modifier = modifier
            .size(70.dp)
            .padding(vertical = 8.dp),
        value = displayText,
        onValueChange = { time: String ->
            val newTimeUnit = validateNumber(time, min, max)
            if (newTimeUnit != null) {
                val newTime = updateTime(newTimeUnit)
                onValueChange(newTime)
            }
        },
        shape = shape,
        placeholder = { Text("00") },
        textStyle = TextStyle(fontSize = 18.sp, textAlign = TextAlign.Center),
        colors = TextFieldDefaults.textFieldColors(
            textColor = colors.textColor,
            containerColor = colors.containerColor,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedLabelColor = colors.unfocusedLabelColor,
            placeholderColor = colors.placeholderColor,


        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}



