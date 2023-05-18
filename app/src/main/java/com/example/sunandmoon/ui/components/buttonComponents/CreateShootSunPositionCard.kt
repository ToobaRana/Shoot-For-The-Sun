package com.example.sunandmoon.ui.components.buttonComponents

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R
import com.example.sunandmoon.ui.components.SunPositionComponent
import com.example.sunandmoon.ui.theme.SunColor
import java.time.LocalTime

@Composable
fun CreateShootSunPositionCard(
    modifier: Modifier,
    containerColor: Color,
    updateTime: (time: LocalTime)->Unit,
    painter: Painter
){
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = containerColor)){
        Icon(painter, "Sun Image", modifier.size(80.dp), SunColor)

    }

}