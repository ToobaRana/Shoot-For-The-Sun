package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sunandmoon.ui.components.SunPositionComponent
import com.example.sunandmoon.R

@Composable
fun SunPositionsCard(
    modifier: Modifier,
    sunriseTime: String,
    solarNoonTime: String,
    sunsetTime: String
) {

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp)
            .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.primary,

            )
    ) {

        Row(
            modifier
                .padding(10.dp)

        ) {
            SunPositionComponent(
                modifier = modifier.weight(1f),
                sunImage = painterResource(R.drawable.sunset),
                sunTime = sunriseTime
            )
            SunPositionComponent(
                modifier = modifier.weight(1f),
                sunImage = painterResource(R.drawable.sun),
                sunTime = solarNoonTime
            )
            SunPositionComponent(
                modifier = modifier.weight(1f),
                sunImage = painterResource(R.drawable.sunset),
                sunTime = sunsetTime
            )
        }
    }


}




