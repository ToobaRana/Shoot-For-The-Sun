package com.example.sunandmoon.ui.components.infoComponents

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R
import com.example.sunandmoon.data.PreferableWeather
import com.example.sunandmoon.data.ProductionSelectionUIState
import com.example.sunandmoon.data.util.Shoot
import com.example.sunandmoon.util.weatherIcons
import com.example.sunandmoon.viewModel.ProductionSelectionViewModel

@Composable
fun PreferredWeatherDialog(modifier: Modifier, productionSelectionViewModel: ProductionSelectionViewModel, shoot: Shoot) {
    AlertDialog(
        onDismissRequest = {
            productionSelectionViewModel.setShowPreferredWeatherDialog(null)
        },
        text = {
            Column() {
                Text(text = "Your preferred weather for this shoot does not match the actual weather of this shoot", fontWeight = FontWeight.Bold)

                Text(text = "Your preferred weather:", fontWeight = FontWeight.Bold)
                PreferredWeatherOverview(modifier, shoot.preferredWeather)
                
                Text(text = "Actual weather:", fontWeight = FontWeight.Bold)

                val weatherIcon = productionSelectionViewModel.getWeatherIconOfShoot(shoot) //weatherIcons[weatherIconSplit[0]]?.get(if(isDay) 0 else 1)
                if(weatherIcon != null) {
                    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(weatherIcon),
                            "Weather Condition Image",
                            modifier.size(80.dp),
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { productionSelectionViewModel.setShowPreferredWeatherDialog(null) }) {
                Text(text = "OK")
            }
        },
        icon = {Icon(
            painter = painterResource(R.drawable.rainthunder),
            "Weather Condition Image",
            modifier.size(50.dp),
        )}
    )
}

@Composable
fun PreferredWeatherOverview(modifier: Modifier, preferableWeathers: List<PreferableWeather>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.padding(0.dp, 15.dp)
    ) {
        items(preferableWeathers) { preferableWeather ->
            var weatherIconId = when (preferableWeather) {
                PreferableWeather.CLEAR_SKY -> R.drawable.clearsun
                PreferableWeather.FAIR -> R.drawable.fairsun
                PreferableWeather.CLOUDY -> R.drawable.cloudy
                PreferableWeather.THUNDER -> R.drawable.rainthunder
                PreferableWeather.RAIN -> R.drawable.rain2
                PreferableWeather.SNOW -> R.drawable.snow
            }
            Icon(
                painter = painterResource(weatherIconId),
                "Weather Condition Image",
                modifier.size(60.dp),
            )
        }
    }
}

