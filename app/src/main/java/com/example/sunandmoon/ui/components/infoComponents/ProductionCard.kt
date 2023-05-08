package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R
import com.example.sunandmoon.data.util.Production
import com.example.sunandmoon.data.util.Shoot

@Composable
fun ProductionCard(modifier: Modifier, production: Production) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(35.dp, 10.dp)
            .clickable { /* TODO */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 15.dp)
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)) {
                Icon(
                    painterResource(R.drawable.bell_icon),
                    "Notification icon",
                    modifier
                        .align(CenterVertically)
                        .padding(8.dp, 0.dp)
                        .size(32.dp),
                    MaterialTheme.colorScheme.onPrimary
                )
                Divider(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp)
                )
                Text(text = production.name, modifier = modifier.padding(16.dp), fontSize = 22.sp)
            }

            Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.background)

            var dateIntervalText = "Empty"
            if (production.duration.first != null && production.duration.second != null) {
                dateIntervalText = production.duration.first?.toLocalDate()
                    .toString() + " - " + production.duration.second?.toLocalDate().toString()
            }
            Text(text = dateIntervalText, modifier = modifier.padding(18.dp), fontSize = 18.sp)
        }
    }
}