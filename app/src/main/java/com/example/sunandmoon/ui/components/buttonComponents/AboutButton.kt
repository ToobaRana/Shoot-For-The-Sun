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

//button for navigating to app-info-screen
@Composable
fun AboutButton(
    modifier: Modifier,
    buttonColor: Color,
    IconColor: Color,
    goToAboutScreen: () -> Unit,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        // the delete and edit buttons
        Row(
            modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            RoundedCornerIconButton(
                modifier,
                {
                    Icon(
                        painterResource(R.drawable.info),
                        stringResource(id = R.string.AboutButton),
                        modifier.size(50.dp),
                        IconColor
                    )
                },
                buttonColor,
                goToAboutScreen
            )
        }
    }
}