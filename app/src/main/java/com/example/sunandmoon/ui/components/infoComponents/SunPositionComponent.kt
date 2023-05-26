package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R
import com.example.sunandmoon.ui.theme.SunColor


//used to draw a icon of the sun and the given time at the bottom
@Composable
fun SunPositionComponent(modifier: Modifier, sunImage: Painter, sunTime: String) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(sunImage, stringResource(id = R.string.SunImage), modifier.size(80.dp), SunColor)
        Text(text = sunTime, fontSize = 18.sp)
    }
}


