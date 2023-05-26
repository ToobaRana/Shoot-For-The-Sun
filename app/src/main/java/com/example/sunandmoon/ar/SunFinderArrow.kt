package com.example.sunandmoon.ar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.sunandmoon.R
import kotlin.math.atan2
import kotlin.math.sqrt

//draws a arrow pointing to the sun using the position of the sun and the orientation of the phone
@Composable
fun SunFinderArrow(
    modifier: Modifier,
    xPos1: Dp,
    yPos1: Dp,
    xPos2: Dp,
    yPos2: Dp,
    sensorStatus: FloatArray
) {
    // checks that the sun is far enough away from the center of the screen
    // (we do not want the arrow to show when the sun is close enough to the center of the screen)

    val closestSunPos = getClosestSunPos(xPos1, yPos1, xPos2, yPos2)
    val distanceToClosestSun = sqrt(closestSunPos[0].value * closestSunPos[0].value + closestSunPos[1].value * closestSunPos[1].value)
    val arrowDistanceThreshold = 100
    // an if-statement inside of an if-statement. This is kotlin. This is incredible
    if(distanceToClosestSun > arrowDistanceThreshold) {
        // arrow pointing towards the sun
        Box(
            modifier = modifier
                .fillMaxSize()
                .fillMaxHeight()
                .fillMaxWidth()
                .rotate((-sensorStatus[2] * 180.0 / Math.PI).toFloat())
                .rotate(
                    atan2(
                        closestSunPos[1].value.toDouble(),
                        closestSunPos[0].value.toDouble()
                    ).toFloat() * (180.0f / Math.PI.toFloat())
                )
        ) {
            Image(painter = painterResource(id = R.drawable.fancy_arrow), contentDescription = "Arrow pointing towards the sun", modifier = modifier
                .align(Alignment.Center)
                .width(70.dp)
                .height(70.dp))
        }
    }
}

//gets the positions of the direction the sun is closest to/which side the arrow will point to
fun getClosestSunPos(xPos1: Dp, yPos1: Dp, xPos2: Dp, yPos2: Dp): List<Dp> {
    val sun1Distance = sqrt(xPos1.value * xPos1.value + yPos1.value * yPos1.value)
    val sun2Distance = sqrt(xPos2.value * xPos2.value + yPos2.value * yPos2.value)
    return if (sun1Distance < sun2Distance) listOf(xPos1, yPos1) else listOf(xPos2, yPos2)
}
