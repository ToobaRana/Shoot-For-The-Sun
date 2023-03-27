package com.example.sunandmoon.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.data.SunUiState
import com.example.sunandmoon.model.SunState
import java.text.DateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableCard(day: String, chosenSunType: String, sunUiState: SunUiState, modifier: Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.White),
            horizontalArrangement = Arrangement.SpaceBetween,

        ) {

                Text(
                    text = day,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )

            if (chosenSunType == "Sunrise"){

                var sunriseTimeListe = sunUiState.sunRiseTime.split("T")
                if (sunriseTimeListe.size >1){
                    var sunriseTime = sunriseTimeListe[1]
                    Text(
                        text = sunriseTime,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp)
                    )

                }


            }

            if (chosenSunType == "Sunset"){
                var sunsetTimeListe = sunUiState.sunSetTime.split("T")
                if (sunsetTimeListe.size >1){
                    var sunsetTime = sunsetTimeListe[1]
                    Text(
                        text = sunsetTime,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp)
                    )

                }

            }
            if (chosenSunType == "SolarNoon"){
                var solarNoonTimeListe = sunUiState.solarNoon.split("T")
                if (solarNoonTimeListe.size >1){
                    var solarNoon = solarNoonTimeListe[1]
                    Text(
                        text = solarNoon,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Text(
                text = "Our result ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(8.dp)
            )

    }
    }
}


