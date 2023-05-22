package com.example.sunandmoon.ui.components.buttonComponents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sunandmoon.R
import com.example.sunandmoon.viewModel.ProductionSelectionViewModel
import com.example.sunandmoon.viewModel.ShootInfoViewModel

@Composable
fun GoBackEditDeleteBar(
    modifier: Modifier,
    buttonColor: Color,
    IconColor: Color,
    goBack: () -> Unit,
    editProduction: () -> Unit,
    deleteProduction: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    Box(modifier = modifier.fillMaxWidth()) {
        // The go back to production selection button
        GoBackButton(modifier, buttonColor, IconColor, goBack)

        // the delete and edit buttons
        Row(
            modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            RoundedCornerIconButton(
                modifier,
                {

                    IconButton(
                        onClick = { showDialog = true })
                     {
                        Icon(
                            painterResource(R.drawable.trash_icon),
                            "Delete-button",
                            modifier.size(50.dp),
                            IconColor
                        )
                    }
                    if (showDialog) {
                        DeleteButton(
                            onConfirmDelete = {
                                showDialog = false
                                deleteProduction()
                            },
                            onCancelDelete = {
                                showDialog = false
                            }

                        )
                    }
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
                        IconColor
                    )
                },
                buttonColor,
                editProduction
            )
        }
    }
}