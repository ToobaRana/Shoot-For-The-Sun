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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.sunandmoon.R
import com.example.sunandmoon.model.locationForecastModel.TimePickerColors
import java.time.LocalTime

//Composable for choosing time for user
@Composable
fun TimepickerComponent(
    modifier: Modifier,
    onValueChange: (chosenTime: LocalTime) -> Unit,
    currentTime: LocalTime,
    enabled: Boolean,
    colors: TimePickerColors,
    fieldShape: Shape,
    containerShape: Shape
    //cursorColor = MaterialTheme.colorScheme.primary,

) {
    val containerColors =
        if (enabled) {
            colors.containerColor
        } else {
            colors.disabledColor
        }
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColors),
        shape = containerShape,
        border = BorderStroke(2.dp, colors.textColor)
    )
    {
        //for centering fields
        Row(
            modifier = modifier.size(width = 150.dp, height = 75.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {

            //puts string for InputField

            InputField(
                modifier = modifier.align(Alignment.CenterVertically),
                displayTextInfo = currentTime.hour,
                onValueChange = { hour: LocalTime -> onValueChange(hour) },
                colors = colors,
                enabled = enabled,
                shape = fieldShape,
                min = 0,
                max = 23,
                updateTime = { hour: Int -> currentTime.withHour(hour) }
            )
            Text(
                modifier = modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                text = ":",
                color = colors.textColor
            )
            //puts string for InputField

            InputField(
                modifier = modifier.align(Alignment.CenterVertically),
                displayTextInfo = currentTime.minute,
                onValueChange = { minute: LocalTime -> onValueChange(minute) },
                shape = fieldShape,
                enabled = enabled,
                colors = colors,
                min = 0,
                max = 59,
                updateTime = { minute: Int -> currentTime.withMinute(minute) }
            )


        }


    }


}

//checks if number is within range and returns null if not
fun validateNumber(value: String, min: Int, max: Int): Int? {

    if (value == "") return 0
    if (Regex("""^\d+$""").matches(value)) {
        return value.toInt().coerceIn(min, max)
    }
    return null

}

//used for input-fields in Timepicker
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier,
    displayTextInfo: Int,
    onValueChange: (chosenTime: LocalTime) -> Unit,
    colors: TimePickerColors,
    shape: Shape,
    enabled: Boolean,
    min: Int,
    max: Int,
    updateTime: (chosenTime: Int) -> LocalTime,

    ) {
    val focusManager = LocalFocusManager.current

    val valueText =
        if (displayTextInfo != 0) {
            displayTextInfo.toString()
        } else {
            ""
        }
    val containerColor =
        if (enabled) {
            colors.containerColor
        } else {
            colors.disabledColor
        }

    TextField(
        modifier = modifier
            .size(70.dp)
            .padding(vertical = 8.dp),
        value = valueText,
        onValueChange = { time: String ->
            val newTimeUnit = validateNumber(time, min, max)
            if (newTimeUnit != null) {
                val newTime = updateTime(newTimeUnit)
                onValueChange(newTime)
            }
        },
        shape = shape,
        enabled = enabled,
        placeholder = { Text("00") },
        textStyle = TextStyle(fontSize = 18.sp, textAlign = TextAlign.Center, fontFamily = FontFamily(Font(R.font.nunito_bold))),
        colors = TextFieldDefaults.textFieldColors(
            textColor = colors.textColor,
            containerColor = containerColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedLabelColor = colors.unfocusedLabelColor,
            placeholderColor = colors.placeholderColor,
            disabledTextColor = colors.textColor,
            disabledIndicatorColor = colors.disabledColor


        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done,

            ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
    )
}


