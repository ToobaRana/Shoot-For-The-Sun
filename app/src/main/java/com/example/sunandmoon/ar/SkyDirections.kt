package com.example.sunandmoon.ar

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp


//draws all sky directions using the compass for direction to show the user which cardinal direction they are facing
@Composable
fun SkyDirections(modifier: Modifier, sensorStatus: FloatArray) {
    val context: Context = LocalContext.current

    val northTextPos: List<Dp> = getARPos(sensorStatus, context, Math.toRadians(0.0), Math.toRadians(0.0))
    SkyDirection(modifier, sensorStatus, northTextPos, "N")

    val northEastTextPos: List<Dp> = getARPos(sensorStatus, context, Math.toRadians(0.0), Math.toRadians(45.0))
    SkyDirection(modifier, sensorStatus, northEastTextPos, "NE")

    val eastTextPos: List<Dp> = getARPos(sensorStatus, context, Math.toRadians(0.0), Math.toRadians(90.0))
    SkyDirection(modifier, sensorStatus, eastTextPos, "E")

    val southEastTextPos: List<Dp> = getARPos(sensorStatus, context, Math.toRadians(0.0), Math.toRadians(135.0))
    SkyDirection(modifier, sensorStatus, southEastTextPos, "SE")

    val south2TextPos: List<Dp> = getARPos(sensorStatus, context, Math.toRadians(0.0), Math.toRadians(180.0))
    SkyDirection(modifier, sensorStatus, south2TextPos, "S")

    val south1TextPos: List<Dp> = getARPos(sensorStatus, context, Math.toRadians(0.0), Math.toRadians(-180.0))
    SkyDirection(modifier, sensorStatus, south1TextPos, "S")

    val southWestTextPos: List<Dp> = getARPos(sensorStatus, context, Math.toRadians(0.0), Math.toRadians(-135.0))
    SkyDirection(modifier, sensorStatus, southWestTextPos, "SW")

    val westTextPos: List<Dp> = getARPos(sensorStatus, context, Math.toRadians(0.0), Math.toRadians(-90.0))
    SkyDirection(modifier, sensorStatus, westTextPos, "W")

    val northWestTextPos: List<Dp> = getARPos(sensorStatus, context, Math.toRadians(0.0), Math.toRadians(-45.0))
    SkyDirection(modifier, sensorStatus, northWestTextPos, "NW")
}

//draws a single sky-direction-box using the offset its given as parameter
@Composable
fun SkyDirection(modifier: Modifier, sensorStatus: FloatArray, pos: List<Dp>, directionText: String) {
    // the text showing a sky direction
    Box(
        modifier = modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth()
            .rotate((-sensorStatus[2] * 180.0 / Math.PI).toFloat())
    ) {
        Text(
            text = directionText,
            modifier = modifier
                .align(Alignment.Center)
                .offset(pos[0], pos[1]),
            color = Color.Black,
            fontSize = 35.sp
        )
    }
}