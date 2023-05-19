package com.example.sunandmoon.ui.components.infoComponents

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
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Dictionary

val weatherIcons : Map<String,List<Int>> = mapOf(
    "clearsky" to listOf(R.drawable.rain, R.drawable.raindrops),
    "cloudy" to listOf(R.drawable.rain, R.drawable.rain),
    "fair" to listOf(R.drawable.rain, R.drawable.rain),
    "fog" to listOf(R.drawable.rain, R.drawable.rain),
    "heavyrain" to listOf(R.drawable.rain, R.drawable.rain),
    "heavyrainandthunder" to listOf(R.drawable.rain, R.drawable.rain),
    "heavyrainshowers" to listOf(R.drawable.rain, R.drawable.rain),
    "heavyrainshowersandthunder" to listOf(R.drawable.rain, R.drawable.rain),
    "heavysleet" to listOf(R.drawable.rain, R.drawable.rain),
    "heavysleetandthunder" to listOf(R.drawable.rain, R.drawable.rain),
    "heavysleetshowers" to listOf(R.drawable.rain, R.drawable.rain),
    "heavysleetshowersandthunder" to listOf(R.drawable.rain, R.drawable.rain),
    "heavysnow" to listOf(R.drawable.rain, R.drawable.rain),
    "heavysnowandthunder" to listOf(R.drawable.rain, R.drawable.rain),
    "heavysnowshowers" to listOf(R.drawable.rain, R.drawable.rain),
    "heavysnowshowersandthunder" to listOf(R.drawable.rain, R.drawable.rain),
    "lightrain" to listOf(R.drawable.rain, R.drawable.rain),
    "lightrainandthunder" to listOf(R.drawable.rain, R.drawable.rain),
    "lightrainshowers" to listOf(R.drawable.rain, R.drawable.rain),
    "lightrainshowersandthunder" to listOf(R.drawable.rain, R.drawable.rain),
    "lightsleet" to listOf(R.drawable.rain, R.drawable.rain),
    "lightsleetandthunder" to listOf(R.drawable.rain, R.drawable.rain),
    "lightsleetshowers" to listOf(R.drawable.rain, R.drawable.rain),
    "lightsnow" to listOf(R.drawable.rain, R.drawable.rain),
    "lightsnowandthunder" to listOf(R.drawable.rain, R.drawable.rain),
    "lightsnowshowers" to listOf(R.drawable.rain, R.drawable.rain),
    "lightssleetshowersandthunder" to listOf(R.drawable.rain, R.drawable.rain),
    "lightssnowshowersandthunder" to listOf(R.drawable.rain, R.drawable.rain),
    "partlycloudy" to listOf(R.drawable.rain, R.drawable.rain),
    "rain" to listOf(R.drawable.rain, R.drawable.rain),
    "rainandthunder" to listOf(R.drawable.rain, R.drawable.rain),
    "rainshowers" to listOf(R.drawable.rain, R.drawable.rain),
    "rainshowersandthunder" to listOf(R.drawable.rain, R.drawable.rain),
    "sleet" to listOf(R.drawable.rain, R.drawable.rain),
    "sleetandthunder" to listOf(R.drawable.rain, R.drawable.rain),
    "sleetshowers" to listOf(R.drawable.rain, R.drawable.rain),
    "sleetshowersandthunder" to listOf(R.drawable.rain, R.drawable.rain),
    "snow" to listOf(R.drawable.rain, R.drawable.rain),
    "snowandthunder" to listOf(R.drawable.rain, R.drawable.rain),
    "snowshowers" to listOf(R.drawable.rain, R.drawable.rain),
    "snowshowersandthunder" to listOf(R.drawable.rain, R.drawable.rain)
)

@Composable
fun WeatherCard(modifier: Modifier, time : LocalTime, temperature : Double?, rainfall : Double?, weatherIconCode : String){
    val weatherIconSplit: List<String> = weatherIconCode.split("_")
    val isDay: Boolean = weatherIconSplit[1] == "day"
    val weatherIcon = weatherIcons[weatherIconSplit[0]]?.get(if(isDay) 0 else 1)


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
                        painter = painterResource(id = weatherIcon),
                        "Weather Condition Image",
                        modifier.size(100.dp),
                        WeatherBlueColor
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
                        Text(text = temperature.toString(), modifier.padding(start = 10.dp), fontSize = 20.sp)

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