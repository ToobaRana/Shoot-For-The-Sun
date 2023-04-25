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

@Composable
fun WeatherCard(modifier: Modifier){

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp)
        //colors = CardDefaults.cardColors(containerColor = Color.DarkGray)

    ){

        Row(modifier.padding(5.dp)) {
            Text(text = "12:15")
        }
        
        Row(
            modifier
                .padding(start = 60.dp, bottom = 10.dp)
                .fillMaxWidth()) {
            //pic of weather
            Image(painter = painterResource(id = R.drawable.rain), contentDescription = "weather condition", modifier.size(80.dp) )
            
            Column(modifier.fillMaxWidth().padding(start = 60.dp)) {
                Row() {

                    //The temperature
                    Image(painter = painterResource(id = R.drawable.thermometer), contentDescription = "thermometer", modifier.size(35.dp) )
                    Text(text = "7°C", modifier.padding(start = 10.dp), fontSize = 20.sp)
                    
                }

                Spacer(modifier.size(15.dp))
                
                Row() {
                    //Rainfall
                    Image(painter = painterResource(id = R.drawable.raindrops), contentDescription = "raindrops", modifier.size(35.dp) )
                    Text(text = "3 mm", modifier.padding(start = 10.dp), fontSize = 20.sp)
                }
            }
            
        }

    }
    
}