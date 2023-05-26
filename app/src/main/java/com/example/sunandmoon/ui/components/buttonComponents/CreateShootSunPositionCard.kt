package com.example.sunandmoon.ui.components.buttonComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sunandmoon.R
import com.example.sunandmoon.ui.theme.RedColor

//used for drawing sunrise sunset and solar noon buttons for updating timepicker according to given prompt
@Composable
fun CreateShootSunPositionCard(
    modifier: Modifier,
    containerColor: Color,
    painter: Painter,
    chosen: Boolean,
    updateTimePicker: () -> Unit,
    updatePositionIndex: () -> Unit,
    iconColor: Color
) {
    val border =
        if (chosen){
            BorderStroke(5.dp, RedColor)
        }else{
            null
        }
    Card(
        modifier = modifier
            .size(80.dp)
            .clickable { updateTimePicker();updatePositionIndex() },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = border
    ) {
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter,
                stringResource(id = R.string.SunImage),
                modifier
                    .size(60.dp),
                tint = iconColor
            )
        }


    }

}