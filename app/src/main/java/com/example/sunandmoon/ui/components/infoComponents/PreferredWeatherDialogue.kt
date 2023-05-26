package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sunandmoon.R
import com.example.sunandmoon.data.PreferableWeather
import com.example.sunandmoon.data.dataUtil.Shoot
import com.example.sunandmoon.ui.theme.CheckmarkColor
import com.example.sunandmoon.ui.theme.RedColor
import com.example.sunandmoon.viewModel.ShootSelectionViewModel

//used to draw the dialogue-box telling a user if the weather matches their choice
@Composable
fun PreferredWeatherDialogue(modifier: Modifier, shootSelectionViewModel: ShootSelectionViewModel, shoot: Shoot) {
    val weatherIconAndContentDescription = shootSelectionViewModel.getWeatherIconOfShoot(shoot) //weatherIcons[weatherIconSplit[0]]?.get(if(isDay) 0 else 1)
    val weatherIcon = weatherIconAndContentDescription?.first
    val weatherContentDescription = weatherIconAndContentDescription?.second
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.primary,
        onDismissRequest = {
            shootSelectionViewModel.setShowPreferredWeatherDialog(null)
        },
        text = {
            Column() {

                val textToShow = if(shoot.weatherMatchesPreferences == true && weatherIcon != null) stringResource(id = R.string.PreferredWeatherMatch)
                    else if( weatherIcon != null) stringResource(id = R.string.PreferredWeatherNotMatch)
                    else stringResource(id = R.string.PreferredWeatherNoWeatherData)
                Text(text = textToShow, fontWeight = FontWeight.Bold, fontFamily = FontFamily(Font(R.font.nunito_bold)))

                Text(text = stringResource(id = R.string.YourPreferredWeather), fontWeight = FontWeight.Bold, fontFamily = FontFamily(Font(R.font.nunito_bold)))
                if(shoot.preferredWeather.isNotEmpty()) {
                    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        PreferredWeatherOverview(modifier, shoot.preferredWeather)
                    }
                }
                else Text(text = stringResource(id = R.string.NoPreferredWeatherRegistered), fontWeight = FontWeight.Bold, modifier = modifier.padding(0.dp, 10.dp), fontFamily = FontFamily(Font(R.font.nunito_bold)))

                if( weatherIcon != null) {
                    Text(text = stringResource(id = R.string.ActualWeather), fontWeight = FontWeight.Bold, fontFamily = FontFamily(Font(R.font.nunito_bold)))

                    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(weatherIcon),
                            weatherContentDescription,
                            modifier.size(100.dp),
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { shootSelectionViewModel.setShowPreferredWeatherDialog(null) }) {
                Text(text = stringResource(id = R.string.OK), fontFamily = FontFamily(Font(R.font.nunito_bold)))
            }
        },
        icon = {Icon(
            painter = painterResource(
                if(shoot.weatherMatchesPreferences == true && weatherIcon != null) R.drawable.check
                else if(weatherIcon != null) R.drawable.warning
                else R.drawable.unavailable
            ),
            tint = if(shoot.weatherMatchesPreferences == true && weatherIcon != null) CheckmarkColor
            else if(weatherIcon != null) RedColor
            else Color.Black,
            contentDescription =  stringResource(id = R.string.preferredWeatherInfo),
            modifier = modifier.size(70.dp),
        )}
    )
}

//used for showing if weather matches choice
@Composable
fun PreferredWeatherOverview(modifier: Modifier, preferableWeathers: List<PreferableWeather>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .padding(0.dp, 15.dp)
            .wrapContentSize(),
        contentPadding = PaddingValues(0.dp, 7.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        //horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            count = preferableWeathers.size,
            span = {
                // to make the last item centered
                if(preferableWeathers.size % 3 != 0 && preferableWeathers.size % 3 != 2){
                    val value = if(preferableWeathers.lastIndex == it) 3 else 1
                    GridItemSpan(value)
                }
                else {
                    GridItemSpan(1)
                }
            }
        ) {
            val weatherIconId = when (preferableWeathers[it]) {
                PreferableWeather.CLEAR_SKY -> R.drawable.clearsun
                PreferableWeather.FAIR -> R.drawable.fairsun
                PreferableWeather.CLOUDY -> R.drawable.cloudy
                PreferableWeather.THUNDER -> R.drawable.rainthunder
                PreferableWeather.RAIN -> R.drawable.rain
                PreferableWeather.SNOW -> R.drawable.snow
            }
            val weatherContentDescription = when (preferableWeathers[it]) {
                PreferableWeather.CLEAR_SKY -> R.string.ClearSky
                PreferableWeather.FAIR -> R.string.Fair
                PreferableWeather.CLOUDY -> R.string.Cloudy
                PreferableWeather.THUNDER -> R.string.Thunderstorm
                PreferableWeather.RAIN -> R.string.Rain
                PreferableWeather.SNOW -> R.string.Snow
            }
            Icon(
                painter = painterResource(weatherIconId),
                stringResource(id = weatherContentDescription),
                modifier.size(60.dp),
            )
        }
    }
}

