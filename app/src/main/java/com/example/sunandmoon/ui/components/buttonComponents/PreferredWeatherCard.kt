package com.example.sunandmoon.ui.components.buttonComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import com.example.sunandmoon.ui.theme.RedColor

//displays buttons for choosing preferred weather.
@Composable
fun PreferredWeatherCard(
    modifier: Modifier,
    containerColor: Color,
    painter: Painter,
    contentDescription: String,
    chosen: Boolean,
    updatePreferredWeather: () -> Unit
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
            .clickable { updatePreferredWeather() },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = border
    ) {
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter,
                contentDescription,
                modifier
                    .size(60.dp)
            )
        }
    }
}