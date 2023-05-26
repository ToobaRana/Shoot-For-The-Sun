package com.example.sunandmoon

import androidx.compose.ui.unit.dp
import com.example.sunandmoon.ar.getClosestSunPos
import com.example.sunandmoon.ar.getFov
import org.junit.Test
import java.text.DecimalFormat


class ArUnitTests {

    @Test
    fun testGetClosestSun(){
        val xPos1 = 10.dp
        val xPos2 = 20.dp
        val yPos1 = 10.dp
        val yPos2 = 20.dp

        val xPos3 = 200.dp
        val xPos4 = 40.dp
        val yPos3 = 53.dp
        val yPos4 = 14.dp


        assert(getClosestSunPos(xPos1, yPos1, xPos2, yPos2) == listOf(xPos1, yPos1) )
        assert(getClosestSunPos(xPos3, yPos3, xPos4, yPos4) == listOf(xPos4, yPos4))

    }




    @Test
    fun testFov(){
        // height = 4.234, width 5.645, focal: 4.2
        val formatter = DecimalFormat("#.##")
        val horizontalFOV = formatter.format(getFov(5.645f, 4.2f)).toDouble()
        val verticalFOV = formatter.format(getFov(4.234f, 4.2f)).toDouble()
        val horizontalFOV2 = formatter.format(getFov(10f, 3.1f)).toDouble()
        val verticalFOV2 = formatter.format(getFov(8.4f, 3.1f)).toDouble()
        val horizontalFOV3 = formatter.format(getFov(1f, 7f)).toDouble()
        val verticalFOV3 = formatter.format(getFov(2f,7f)).toDouble()

        //numbers fetched from https://www.omnicalculator.com/other/camera-field-of-view
        assert(horizontalFOV == 67.8)
        assert(verticalFOV == 53.5)
        assert(horizontalFOV2 == 116.4)
        assert(verticalFOV2 == 107.14)
        assert(horizontalFOV3 == 8.17)
        assert(verticalFOV3 == 16.26)
    }
}