package com.example.sunandmoon.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableCard(sunType: String, apiSunTime: String, calculationSunTime: String, day: String, offset: String, modifier: Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.White)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalArrangement = Arrangement.Start,

            ) {

            Text(
                text = day,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = modifier.padding(8.dp)
            )
            if (sunType == "SolarNoon"){
                Text(
                    text = apiSunTime,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = modifier.padding(8.dp)
                )

            }
            if (sunType == "Sunset" || sunType == "Sunrise"){
                Text(
                    text = apiSunTime,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = modifier.padding(top =8.dp)
                )

            }



            Text(
                text = calculationSunTime,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = modifier.padding(top= 8.dp, start = 20.dp)
            )

            Text(
                text = offset,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = modifier.padding(top= 8.dp, start = 45.dp)
            )

        }
    }
}


