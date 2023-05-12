package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R
import com.example.sunandmoon.data.util.Shoot

@Composable
fun ShootCard(modifier: Modifier, shoot: Shoot, navigateToNext: (shoot: Shoot) -> Unit) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(35.dp, 10.dp)
            .clickable {
                navigateToNext(shoot)
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
                Text(text = shoot.name, modifier = modifier.padding(16.dp), fontSize = 22.sp)
            }

            Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.background)

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Text(text = shoot.date.toLocalDate().toString(), fontSize = 18.sp, modifier = modifier.weight(1f))
                Text(text = shoot.date.toLocalTime().toString(), textAlign = TextAlign.End, fontSize = 18.sp, modifier = modifier.weight(1f))
            }
        }
    }
}