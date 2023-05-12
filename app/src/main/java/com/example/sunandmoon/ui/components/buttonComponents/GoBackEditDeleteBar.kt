package com.example.sunandmoon.ui.components.buttonComponents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sunandmoon.R

@Composable
fun GoBackEditDeleteBar(
    modifier: Modifier,
    buttonColor: Color,
    goBack: () -> Unit,
    editProduction: () -> Unit,
    deleteProduction: () -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        // The go back to production selection button
        GoBackButton(modifier, buttonColor, goBack)

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
                        painterResource(R.drawable.trash_icon),
                        "Delete-button",
                        modifier.size(50.dp),
                        MaterialTheme.colorScheme.background
                    )
                },
                buttonColor,
                deleteProduction
            )
            RoundedCornerIconButton(
                modifier,
                {
                    Icon(
                        painterResource(R.drawable.edit_icon),
                        "Edit-button",
                        modifier.size(50.dp),
                        MaterialTheme.colorScheme.background
                    )
                },
                buttonColor,
                editProduction
            )
        }
    }
}