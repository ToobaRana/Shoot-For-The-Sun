package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R
import com.example.sunandmoon.ui.theme.WeatherBlueColor
import java.time.LocalTime

//card for displaying wind in shootInfoScreen
@Composable
fun WindCard(modifier: Modifier, time: LocalTime, windSpeed : Double?, windDirection : Double?) {

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

        Box(
            modifier = modifier
                .padding(15.dp)
        ) {

            Text(text = time.toString(), fontSize = 18.sp)


            Row(
                modifier
                    .padding(start = 50.dp, top = 20.dp)
                    .fillMaxWidth()
            ) {


                Spacer(modifier = modifier.size(20.dp))

                Column(
                ) {
                    //The wind description
                    Icon(
                        painter = painterResource(id = R.drawable.wind),
                        stringResource(id = R.string.WindImage),
                        modifier
                            .size(60.dp)
                            .padding(top = 5.dp),
                        WeatherBlueColor
                    )
                    Text(text = windSpeed.toString() + " m/s", fontSize = 20.sp, modifier = modifier.padding(start = 5.dp))
                }
                
                Spacer(modifier = modifier.size(30.dp))
                if(windDirection != null) {
                    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.wind_compass),
                            stringResource(id = R.string.WindCompass),
                            modifier.fillMaxSize()
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.wind_arrow),
                            stringResource(id = R.string.WindCompass),
                            modifier
                                .fillMaxSize(0.2f)
                                .padding(start = 2.dp, bottom = 1.dp)
                                .rotate(-windDirection.toFloat())
                        )
                    }
                }
            }
        }
    }
}