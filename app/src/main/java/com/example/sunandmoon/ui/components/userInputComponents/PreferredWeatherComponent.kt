package com.example.sunandmoon.ui.components.userInputComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.data.PreferableWeather
import com.example.sunandmoon.ui.components.buttonComponents.PreferredWeatherCard
import com.example.sunandmoon.ui.theme.GreyColor
import com.example.sunandmoon.viewModel.CreateShootViewModel

//for choosing preferred weather in create shoot
@Composable
fun PreferredWeatherComponent(
    modifier: Modifier,
    preferredWeather: List<PreferableWeather>,
    createShootViewModel: CreateShootViewModel = hiltViewModel()
) {

    Card(
        modifier.fillMaxWidth(0.9f),
        colors = CardDefaults.cardColors(containerColor = GreyColor)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 15.dp, start = 15.dp, end = 15.dp, bottom = 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val color = MaterialTheme.colorScheme.secondary

            PreferredWeatherCard(
                modifier = modifier,

                painter = painterResource(
                    R.drawable.clearsun
                ),
                contentDescription = stringResource(id =R.string.ClearSky),
                containerColor = color,
                chosen = PreferableWeather.CLEAR_SKY in preferredWeather,
                updatePreferredWeather = {createShootViewModel.setUnsetPreferredWeather(PreferableWeather.CLEAR_SKY)}
            )

            PreferredWeatherCard(
                modifier = modifier,
                painter = painterResource(
                    R.drawable.fairsun
                ),
                contentDescription = stringResource(id =R.string.Fair),
                containerColor = color,
                chosen = PreferableWeather.FAIR in preferredWeather,
                updatePreferredWeather = {createShootViewModel.setUnsetPreferredWeather(PreferableWeather.FAIR)}

            )
            PreferredWeatherCard(
                modifier = modifier,
                painter = painterResource(
                    R.drawable.cloudy
                ),
                contentDescription = stringResource(id =R.string.Cloudy),
                containerColor = color,
                chosen = PreferableWeather.CLOUDY in preferredWeather,
                updatePreferredWeather = {createShootViewModel.setUnsetPreferredWeather(PreferableWeather.CLOUDY)}
            )
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val color = MaterialTheme.colorScheme.secondary

            PreferredWeatherCard(
                modifier = modifier,

                painter = painterResource(
                    R.drawable.rain
                ),
                contentDescription = stringResource(id =R.string.Rain),
                containerColor = color,
                chosen = PreferableWeather.RAIN in preferredWeather,
                updatePreferredWeather = {createShootViewModel.setUnsetPreferredWeather(PreferableWeather.RAIN)}
            )

            PreferredWeatherCard(
                modifier = modifier,
                painter = painterResource(
                    R.drawable.rainthunder
                ),
                contentDescription = stringResource(id =R.string.Thunderstorm),
                containerColor = color,
                chosen = PreferableWeather.THUNDER in preferredWeather,
                updatePreferredWeather = {createShootViewModel.setUnsetPreferredWeather(PreferableWeather.THUNDER)}

            )
            PreferredWeatherCard(
                modifier = modifier,
                painter = painterResource(
                    R.drawable.snow
                ),
                contentDescription = stringResource(id =R.string.Snow),
                containerColor = color,
                chosen = PreferableWeather.SNOW in preferredWeather,
                updatePreferredWeather = {createShootViewModel.setUnsetPreferredWeather(PreferableWeather.SNOW)}
            )
        }
    }

}