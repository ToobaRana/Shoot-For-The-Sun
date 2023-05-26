package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//Used for displaying api-times vs our calculation-times
@Composable
fun TableCard(apiSunTime: String, calculationSunTime: String, day: String, offset: String, modifier: Modifier) {
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

            ) {

            Text(
                text = day,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = modifier.padding(top = 10.dp, bottom = 10.dp, start = 10.dp).weight(1.5f)
            )

                Text(
                    text = apiSunTime,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = modifier.padding(top = 10.dp, bottom = 10.dp).weight(0.7f)
                )

            Text(
                text = calculationSunTime,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = modifier.padding(top= 10.dp, bottom = 10.dp).weight(0.7f)
            )

            Text(
                text = offset,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = modifier.padding(top= 10.dp, bottom = 10.dp).weight(0.7f)
            )

        }
    }
}


