package com.example.sunandmoon.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sunandmoon.R


@Composable
fun SunPositionComponent(modifier: Modifier, sunImage: Painter, sunTime: String){

    Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){


            Image(painter = sunImage, contentDescription = "Image", modifier.size(80.dp) )
            Text(text = sunTime)

        }
}


