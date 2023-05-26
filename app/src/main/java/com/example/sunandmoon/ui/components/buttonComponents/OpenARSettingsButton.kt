package com.example.sunandmoon.ui.components.buttonComponents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sunandmoon.R


//opens ar settings for changing position and time
@Composable
fun OpenARSettingsButton(
    modifier: Modifier,
    buttonColor: Color,
    IconColor: Color,
    openARSettings: () -> Unit,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        // the delete and edit buttons
        Row(
            modifier
                .fillMaxWidth().padding(top = 10.dp),
            horizontalArrangement = Arrangement.End
        ) {
            RoundedCornerIconButton(
                modifier,
                {
                    Icon(
                        painterResource(R.drawable.settings),
                        stringResource(id = R.string.OpenARSettingsButton),
                        modifier.size(50.dp),
                        IconColor
                    )
                },
                buttonColor,
                openARSettings
            )
        }
    }
}