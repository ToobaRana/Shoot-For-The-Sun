package com.example.sunandmoon.ui.components.buttonComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.example.sunandmoon.ui.theme.SunColor

@Composable
fun CreateShootSunPositionCard(
    modifier: Modifier,
    containerColor: Color,
    updateTime:  ()->Unit,
    painter: Painter,
    updateTimePicker: ()->Unit

) {
    Card(
        modifier = modifier.clickable { updateTime(); updateTimePicker()},
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Icon(painter, "Sun Image", modifier.size(80.dp), SunColor)

    }

}