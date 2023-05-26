package com.example.sunandmoon.ui.components.buttonComponents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sunandmoon.R

//top bar with go back, edit and delete
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
                            stringResource(id = R.string.DeleteButton),
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