package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R
import com.example.sunandmoon.ui.theme.WeatherBlueColor
import java.time.LocalTime

@Composable
fun WindCard(modifier: Modifier, time: LocalTime, windSpeed : Double?, WindDirection : Double?) {

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

            Text(text = time.toString(), fontSize = 18.sp)


            Row(
                modifier
                    .padding(start = 60.dp, top = 5.dp)
                    .fillMaxWidth()
            ) {


                Spacer(modifier = modifier.size(25.dp))

                Column(
                ) {
                    //The wind description
                    Icon(
                        painter = painterResource(id = R.drawable.wind),
                        "Wind Image",
                        modifier
                            .size(60.dp),
                        WeatherBlueColor
                    )
                    Text(text = windSpeed.toString() + " m/s", fontSize = 20.sp, modifier = modifier.padding(start = 5.dp))
                }
                
                Spacer(modifier = modifier.size(60.dp))


                //pic of wind compass
                Icon(
                    painter = painterResource(id = R.drawable.windcompass),
                    "Wind Image",
                    modifier.size(1800.dp)
                )

            }
        }
    }
}