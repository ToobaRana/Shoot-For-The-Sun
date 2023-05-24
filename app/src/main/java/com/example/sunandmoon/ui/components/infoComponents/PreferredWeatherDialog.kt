package com.example.sunandmoon.ui.components.infoComponents

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R
import com.example.sunandmoon.data.PreferableWeather
import com.example.sunandmoon.data.ProductionSelectionUIState
import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.ui.theme.CheckmarkColor
import com.example.sunandmoon.util.weatherIcons
import androidx.compose.foundation.Image
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.sunandmoon.ui.theme.RedColor
import com.example.sunandmoon.viewModel.ProductionSelectionViewModel

@Composable
fun PreferredWeatherDialog(modifier: Modifier, productionSelectionViewModel: ProductionSelectionViewModel, shoot: Shoot) {
    val weatherIcon = productionSelectionViewModel.getWeatherIconOfShoot(shoot) //weatherIcons[weatherIconSplit[0]]?.get(if(isDay) 0 else 1)
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.primary,
        onDismissRequest = {
            productionSelectionViewModel.setShowPreferredWeatherDialog(null)
        },
        text = {
            Column() {

                val textToShow = if(shoot.weatherMatchesPreferences == true && weatherIcon != null) "Your preferred weather for this shoot matches the actual weather of this shoot"
                    else if( weatherIcon != null) "Your preferred weather for this shoot does not match the actual weather of this shoot"
                    else "No weather data available for this shoot, weather data is available from the current date to 10 days into the future"
                Text(text = textToShow, fontWeight = FontWeight.Bold, fontFamily = FontFamily(Font(R.font.nunito_bold)))

                Text(text = "Your preferred weather:", fontWeight = FontWeight.Bold, fontFamily = FontFamily(Font(R.font.nunito_bold)))
                if(shoot.preferredWeather.isNotEmpty()) {
                    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        PreferredWeatherOverview(modifier, shoot.preferredWeather)
                    }
                }
                else Text(text = "(No preferred weather registered)", fontWeight = FontWeight.Bold, modifier = modifier.padding(0.dp, 10.dp), fontFamily = FontFamily(Font(R.font.nunito_bold)))

                if( weatherIcon != null) {
                    Text(text = "Actual weather:", fontWeight = FontWeight.Bold, fontFamily = FontFamily(Font(R.font.nunito_bold)))

                    if(weatherIcon != null) {
                        Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(weatherIcon),
                                "Weather Condition Image",
                                modifier.size(100.dp),
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { productionSelectionViewModel.setShowPreferredWeatherDialog(null) }) {
                Text(text = "Ok", fontFamily = FontFamily(Font(R.font.nunito_bold)))
            }
        },
        icon = {Icon(
            painter = painterResource(if(shoot.weatherMatchesPreferences == true) R.drawable.check else R.drawable.warning),
            tint = if(shoot.weatherMatchesPreferences == true && weatherIcon != null) CheckmarkColor
            else if(weatherIcon != null) RedColor
            else Color.Black,
            contentDescription =  "Preferred weather info",
            modifier = modifier.size(70.dp),
        )}
    )
}

@Composable
fun PreferredWeatherOverview(modifier: Modifier, preferableWeathers: List<PreferableWeather>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.padding(0.dp, 15.dp).wrapContentSize(),
        contentPadding = PaddingValues(0.dp, 7.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        //horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            count = preferableWeathers.size,
            span = {
                // to make the last item centered
                if(preferableWeathers.size % 3 != 0 && preferableWeathers.size % 3 != 2){
                    var value = if(preferableWeathers.lastIndex == it) 3 else 1
                    GridItemSpan(value)
                }
                else {
                    GridItemSpan(1)
                }
            }
        ) {
            var weatherIconId = when (preferableWeathers[it]) {
                PreferableWeather.CLEAR_SKY -> R.drawable.clearsun
                PreferableWeather.FAIR -> R.drawable.fairsun
                PreferableWeather.CLOUDY -> R.drawable.cloudy
                PreferableWeather.THUNDER -> R.drawable.rainthunder
                PreferableWeather.RAIN -> R.drawable.rain
                PreferableWeather.SNOW -> R.drawable.snow
            }
            Image(
                painter = painterResource(weatherIconId),
                "Weather Condition Image",
                modifier.size(60.dp),
            )
        }
    }
}

