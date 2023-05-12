package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R
import com.example.sunandmoon.ui.components.SunPositionComponent
import java.time.LocalDateTime
import java.time.LocalTime


@Composable
fun WeatherCard(modifier: Modifier, time : LocalTime, temperature : Double?){

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {

        Column(
            modifier = modifier
                .padding(15.dp)
        ) {

            Text(text = time.toString())

            Row(
                modifier
                    .padding(start = 60.dp, bottom = 10.dp)
                    .fillMaxWidth()
            ){

                //pic of weather
                Image(
                    painter = painterResource(id = R.drawable.rain),
                    contentDescription = "weather condition",
                    modifier = modifier.size(80.dp)
                )

                Column(
                    modifier
                        .fillMaxWidth()
                        .padding(start = 60.dp)
                ){

                    Row(){

                        //The temperature
                        Image(painter = painterResource(id = R.drawable.thermometer), contentDescription = "thermometer", modifier = modifier.size(35.dp))
                        Text(text = temperature.toString(), modifier.padding(start = 10.dp), fontSize = 20.sp)

                    }

                    Spacer(modifier.size(15.dp))

                    Row() {
                        //Rainfall
                        Image(painter = painterResource(id = R.drawable.raindrops), contentDescription = "raindrops", modifier = modifier.size(35.dp))
                        Text(text = "3 mm", modifier.padding(start = 10.dp), fontSize = 20.sp)
                    }
                }
            }
        }
    }
}