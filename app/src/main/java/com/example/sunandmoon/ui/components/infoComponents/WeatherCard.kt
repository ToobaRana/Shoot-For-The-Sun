package com.example.sunandmoon.ui.components.infoComponents

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R
import com.example.sunandmoon.ui.theme.SunColor
import com.example.sunandmoon.ui.theme.ThermometerColor
import com.example.sunandmoon.ui.theme.WeatherBlueColor
import com.example.sunandmoon.util.weatherIcons
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Dictionary

@Composable
fun WeatherCard(modifier: Modifier, time : LocalTime, temperature : Double?, rainfall : Double?, weatherIcon : Int?){


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
                    .padding(start = 60.dp, bottom = 10.dp)
                    .fillMaxWidth()
            ) {

                //pic of weather weatherIcons[]
                if(weatherIcon != null){
                    Icon(
                        painter = painterResource(weatherIcon),
                        "Weather Condition Image",
                        modifier.size(80.dp),
                    )
                }


                Column(
                    modifier
                        .fillMaxWidth()
                        .padding(start = 60.dp)
                ) {

                    Row() {

                        //The temperature
                        Icon(
                            painter = painterResource(id = R.drawable.thermometer),
                            "Thermometer Image",
                            modifier.size(35.dp),
                            ThermometerColor
                        )
                        Text(text = temperature.toString() + "°C", modifier.padding(start = 10.dp), fontSize = 20.sp)

                    }

                    Spacer(modifier.size(15.dp))

                    Row() {
                        //Rainfall
                        Icon(
                            painter = painterResource(id = R.drawable.raindrops),
                            "Raindrops Image",
                            modifier.size(35.dp),
                            WeatherBlueColor
                        )
                        Text(text = rainfall.toString() + " mm", modifier.padding(start = 10.dp), fontSize = 20.sp)
                    }
                }
            }
        }
    }
}