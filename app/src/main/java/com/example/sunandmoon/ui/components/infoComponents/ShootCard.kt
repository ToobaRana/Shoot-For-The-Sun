package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sunandmoon.data.util.Shoot

@Composable
fun ShootCard(modifier: Modifier, shoot: Shoot, navigateToNext: (shoot: Shoot) -> Unit) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(35.dp, 10.dp)
            .clickable { navigateToNext(shoot) },
        elevation = CardDefaults.cardElevation(defaultElevation = 15.dp)
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = shoot.name, modifier = modifier.padding(20.dp))
            Text(text = shoot.date.toLocalDate().toString(), modifier = modifier.padding(20.dp))
        }
    }
}