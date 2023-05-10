package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R
import com.example.sunandmoon.ui.theme.UVLowColor
import com.example.sunandmoon.ui.theme.WeatherBlueColor
import java.time.LocalTime

@Composable
fun UVCard(modifier: Modifier, time: LocalTime) {

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp)
            .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.primary
        )


    ) {

        Column(
            modifier = modifier
                .padding(15.dp)
        ) {

            Text(text = time.toString())

            Row(
                modifier
                    .padding(start = 60.dp, top = 5.dp)
                    .fillMaxWidth()
            ) {

                Spacer(modifier.size(10.dp))


                //UV icon that symbolises how intense the UV-rays are
                Icon(
                    painter = painterResource(id = R.drawable.sun),
                    "UV-Intensity Based Image",
                    modifier.size(100.dp),
                    UVLowColor
                )


                Spacer(modifier.size(30.dp))

                Column(
                ) {

                    //UV info
                    Text(text = "UV-index: 1", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier.size(10.dp))
                    Text(text = "No need for protection", fontSize = 15.sp)
                }
            }
        }
    }
}