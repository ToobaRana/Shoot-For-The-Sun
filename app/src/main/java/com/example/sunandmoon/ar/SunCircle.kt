package com.example.sunandmoon.ar

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs

//Draws the circle used for showing the user where the sun is at the specified time and date
@Composable
fun SunCircle(
    modifier: Modifier,
    sensorStatus: FloatArray,
    xPos: Dp,
    yPos: Dp,
    color: Color,
    sunZenith: Double
) {
    // the sun
    Box(
        modifier = modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth()
            .rotate((-sensorStatus[2] * 180.0 / Math.PI).toFloat())
    ) {
        val sensitivityAdjuster = (1 - abs((sunZenith%90.0)/90.0)).toFloat()
        //Log.i("sensitivityAdjuster", sensitivityAdjuster.toString())
        Spacer(
            modifier = modifier
                .align(Alignment.Center)
                .offset(xPos * sensitivityAdjuster, yPos)
                //.background(Color.Red, CircleShape)
                .border(7.dp, color, CircleShape)
                .width(240.dp)
                .height(240.dp)
        )
    }
}