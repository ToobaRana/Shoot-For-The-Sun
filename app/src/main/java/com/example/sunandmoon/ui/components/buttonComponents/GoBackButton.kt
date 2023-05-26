package com.example.sunandmoon.ui.components.buttonComponents

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sunandmoon.R

//top bar used to navigate back to previous screen
@Composable
fun GoBackButton(
    modifier: Modifier,
    buttonColor: Color,
    IconColor: Color,
    goBack: () -> Unit
) {
    Button(
        modifier = modifier
            .height(40.dp)
            .width(95.dp)
            .padding(start = 10.dp, top = 5.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        ),
        onClick = goBack
    ) {
        Icon(
            painterResource(R.drawable.arrow_icon),
            stringResource(id = R.string.DeleteButton),
            modifier.fillMaxSize(),
            IconColor
        )
    }
}