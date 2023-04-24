package com.example.sunandmoon.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp


@Composable
fun SunCard(modifier: Modifier, displayElement : String, sunImage: Painter, sunTime: String){

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp),
        //colors = CardDefaults.cardColors(containerColor = Color.DarkGray)

    ){
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Text(text = displayElement, modifier = modifier.padding(20.dp))
            Image(painter = sunImage, contentDescription = "Image", modifier.size(80.dp) )
            Text(text = sunTime, modifier = modifier.padding(20.dp))

        }
    }

}
