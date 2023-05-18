package com.example.sunandmoon.ui.components.userInputComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.sunandmoon.R
import com.example.sunandmoon.ui.components.buttonComponents.CreateShootSunPositionCard
import java.time.LocalTime

@Composable
fun SunPositionTime(modifier: Modifier, updateTime: (time: LocalTime) -> Unit) {
    Card(
        modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {

            val color = MaterialTheme.colorScheme.tertiary

            CreateShootSunPositionCard(
                modifier = modifier, updateTime = {
                    updateTime(
                        LocalTime.now()
                    )
                }, painter = painterResource(
                    R.drawable.sunset
                ), containerColor = color
            )
            CreateShootSunPositionCard(
                modifier = modifier,
                updateTime = { time: LocalTime -> updateTime(time) },
                painter = painterResource(
                    R.drawable.sunrise
                ),
                containerColor = color
            )
            CreateShootSunPositionCard(
                modifier = modifier,
                updateTime = { time: LocalTime -> updateTime(time) },
                painter = painterResource(
                    R.drawable.solarnoon
                ),
                containerColor = color
            )
            CreateShootSunPositionCard(
                modifier = modifier,
                updateTime = { time: LocalTime -> updateTime(time) },
                painter = painterResource(
                    R.drawable.sunset
                ),
                containerColor = color
            )
        }
    }

}