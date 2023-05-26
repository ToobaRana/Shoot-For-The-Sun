package com.example.sunandmoon.ui.components.buttonComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R
import com.example.sunandmoon.data.dataUtil.Shoot
import com.example.sunandmoon.ui.theme.CheckmarkColor
import com.example.sunandmoon.ui.theme.RedColor

//used for displaying shoots on cards in all shoots and in my shoots
@Composable
fun ShootCard(modifier: Modifier, shoot: Shoot, navigateToNext: (shootId: Int) -> Unit, openPreferredWeatherDialog: () -> Unit) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp, 10.dp)
            .clickable {
                shoot.id?.let { navigateToNext(it) }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 15.dp)
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                val iconColor: Color
                val weatherCheckerIcon: Int?

                if(shoot.weatherMatchesPreferences != null) {
                    if(shoot.weatherMatchesPreferences){
                        weatherCheckerIcon = R.drawable.check
                        iconColor = CheckmarkColor
                    }
                    else {
                        weatherCheckerIcon = R.drawable.warning
                        iconColor = RedColor

                    }
                } else {
                    weatherCheckerIcon = R.drawable.unavailable
                    iconColor = MaterialTheme.colorScheme.secondary
                }


                Icon(
                    painterResource(weatherCheckerIcon),
                    stringResource(id = R.string.WeatherCheckerIcon),
                    modifier
                        .align(CenterVertically)
                        .padding(8.dp, 0.dp)
                        .size(40.dp)
                        .clickable {
                            openPreferredWeatherDialog()
                        },
                    iconColor
                )
                Divider(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp)
                )
                Text(text = shoot.name, modifier = modifier.padding(16.dp), fontSize = 22.sp)
            }

            Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.background)

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                val date = shoot.dateTime.toLocalDate()
                Text(text = "${date.dayOfMonth}. ${date.month.toString().substring(0, 3)} ${date.year}", fontSize = 18.sp, modifier = modifier.weight(1f))
                Text(text = shoot.dateTime.toLocalTime().toString(), textAlign = TextAlign.End, fontSize = 18.sp, modifier = modifier.weight(1f))
            }
        }
    }
}