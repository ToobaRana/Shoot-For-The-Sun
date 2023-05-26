package com.example.sunandmoon.ar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import com.example.sunandmoon.ui.theme.RedColor

//puts the skydirections arrows, the sun circles and the arrow for finding the sun on the screen
@Composable
fun SunFinder(
    modifier: Modifier,
    sensorStatus: FloatArray,
    sunZenithParam: Double,
    sunAzimuthParam: Double
) {

    if(sensorStatus.size < 2 || sensorStatus[0].isNaN() || sensorStatus[1].isNaN()) return

    // sunZenith and sunAzimuth are in degrees
    val sunZenith: Double = sunZenithParam //41.19
    val sunAzimuth: Double = sunAzimuthParam //137.87

    val sun1ImagePos: List<Dp> = getARPos(sensorStatus, LocalContext.current, Math.toRadians(sunZenith), Math.toRadians(-sunAzimuth))
    val xPos1 = sun1ImagePos[0]
    val yPos1 = sun1ImagePos[1]

    val sun2ImagePos: List<Dp> = getARPos(sensorStatus, LocalContext.current, Math.toRadians(sunZenith), Math.toRadians(360 - sunAzimuth))
    val xPos2 = sun2ImagePos[0]
    val yPos2 = sun2ImagePos[1]

    /*Column() {
        Column() {
            for(element in sensorStatus) {
                Text(text = "        " + (element * 180.0 / Math.PI).toString(), color = Color.White)
            }
        }
        Column() {
            for(element in sensorStatus) {
                Text(text = "        " + (element * 180.0 / Math.PI).toString(), color = Color.Black)
            }
        }
    }*/
    //println(sensorStatus[2] * 180.0 / Math.PI)

    // sky directions
    SkyDirections(modifier, sensorStatus)

    // the sun
    SunCircle(modifier, sensorStatus, xPos1, yPos1, RedColor, sunZenith)
    // the "other" sun
    SunCircle(modifier, sensorStatus, xPos2, yPos2, RedColor, sunZenith)

    SunFinderArrow(modifier, xPos1, yPos1, xPos2, yPos2, sensorStatus)
}