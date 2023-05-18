package com.example.sunandmoon.ui.components.userInputComponents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sunandmoon.R
import com.example.sunandmoon.getSunRiseNoonFall
import com.example.sunandmoon.ui.components.buttonComponents.CreateShootSunPositionCard
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

@Composable
fun SunPositionTime(modifier: Modifier, updateTime: (time: LocalTime) -> Unit, sunTimes: List<String>) {
    Card(
        modifier.fillMaxWidth(0.95f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(modifier = modifier.fillMaxWidth().padding(15.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {

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
                updateTime = { updateTime(LocalTime.parse(sunTimes[0])) },
                painter = painterResource(
                    R.drawable.sunrise
                ),
                containerColor = color,

            )
            CreateShootSunPositionCard(
                modifier = modifier,
                updateTime = {  updateTime(LocalTime.parse(sunTimes[1])) },
                painter = painterResource(
                    R.drawable.solarnoon
                ),
                containerColor = color
            )
            CreateShootSunPositionCard(
                modifier = modifier,
                updateTime = { updateTime(LocalTime.parse(sunTimes[2])) },
                painter = painterResource(
                    R.drawable.sunset
                ),
                containerColor = color
            )
        }
    }

}