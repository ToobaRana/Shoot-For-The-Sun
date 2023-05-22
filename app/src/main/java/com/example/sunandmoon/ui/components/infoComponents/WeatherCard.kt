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
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Dictionary

val weatherIcons : Map<String,List<Int>> = mapOf(
    "clearsky" to listOf(R.drawable.clearsun, R.drawable.clearnight),
    "cloudy" to listOf(R.drawable.cloudy, R.drawable.cloudy),
    "fair" to listOf(R.drawable.fairsun, R.drawable.fairnight),
    "fog" to listOf(R.drawable.fog, R.drawable.fog),
    "heavyrain" to listOf(R.drawable.heavyrain, R.drawable.heavyrain),
    "heavyrainandthunder" to listOf(R.drawable.heavyrainthunder, R.drawable.heavyrainthunder),
    "heavyrainshowers" to listOf(R.drawable.heavyrain, R.drawable.heavyrain),
    "heavyrainshowersandthunder" to listOf(R.drawable.heavyrainthunder, R.drawable.heavyrainthunder),
    "heavysleet" to listOf(R.drawable.heavysleet, R.drawable.heavysleet),
    "heavysleetandthunder" to listOf(R.drawable.heavyrainthunder, R.drawable.heavyrainthunder),
    "heavysleetshowers" to listOf(R.drawable.heavysleet, R.drawable.heavysleet),
    "heavysleetshowersandthunder" to listOf(R.drawable.heavyrainthunder, R.drawable.heavyrainthunder),
    "heavysnow" to listOf(R.drawable.snow, R.drawable.snow),
    "heavysnowandthunder" to listOf(R.drawable.heavyrainthunder, R.drawable.heavyrainthunder),
    "heavysnowshowers" to listOf(R.drawable.snow, R.drawable.snow),
    "heavysnowshowersandthunder" to listOf(R.drawable.heavyrainthunder, R.drawable.heavyrainthunder),
    "lightrain" to listOf(R.drawable.rain, R.drawable.rain),
    "lightrainandthunder" to listOf(R.drawable.rainthunder, R.drawable.rainthunder),
    "lightrainshowers" to listOf(R.drawable.rain, R.drawable.rain),
    "lightrainshowersandthunder" to listOf(R.drawable.rainthunder, R.drawable.rainthunder),
    "lightsleet" to listOf(R.drawable.lightsleet, R.drawable.lightsleet),
    "lightsleetandthunder" to listOf(R.drawable.rainthunder, R.drawable.rainthunder),
    "lightsleetshowers" to listOf(R.drawable.lightsleet, R.drawable.lightsleet),
    "lightsnow" to listOf(R.drawable.snow, R.drawable.snow),
    "lightsnowandthunder" to listOf(R.drawable.rainthunder, R.drawable.rainthunder),
    "lightsnowshowers" to listOf(R.drawable.snow, R.drawable.snow),
    "lightssleetshowersandthunder" to listOf(R.drawable.rainthunder, R.drawable.rainthunder),
    "lightssnowshowersandthunder" to listOf(R.drawable.rainthunder, R.drawable.rainthunder),
    "partlycloudy" to listOf(R.drawable.partlycloudly, R.drawable.partlycloudly),
    "rain" to listOf(R.drawable.rain, R.drawable.rain),
    "rainandthunder" to listOf(R.drawable.rainthunder, R.drawable.rainthunder),
    "rainshowers" to listOf(R.drawable.rain, R.drawable.rain),
    "rainshowersandthunder" to listOf(R.drawable.rainthunder, R.drawable.rainthunder),
    "sleet" to listOf(R.drawable.lightsleet, R.drawable.lightsleet),
    "sleetandthunder" to listOf(R.drawable.rainthunder, R.drawable.rainthunder),
    "sleetshowers" to listOf(R.drawable.lightsleet, R.drawable.lightsleet),
    "sleetshowersandthunder" to listOf(R.drawable.rainthunder, R.drawable.rainthunder),
    "snow" to listOf(R.drawable.snow, R.drawable.snow),
    "snowandthunder" to listOf(R.drawable.rainthunder, R.drawable.rainthunder),
    "snowshowers" to listOf(R.drawable.snow, R.drawable.snow),
    "snowshowersandthunder" to listOf(R.drawable.rainthunder, R.drawable.rainthunder)
)

@Composable
fun WeatherCard(modifier: Modifier, time : LocalTime, temperature : Double?, rainfall : Double?, weatherIconCode : String){
    val weatherIconSplit: List<String> = weatherIconCode.split("_")
    Log.i("weatherIcon", weatherIconCode)
    val isDay: Boolean = weatherIconSplit.size < 2 || weatherIconSplit[1] == "day"
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