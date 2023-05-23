package com.example.sunandmoon.ui.components.buttonComponents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteButton(
    onConfirmDelete: () -> Unit,
    onCancelDelete: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancelDelete,
        title = {
            Text(
                text = "Delete",
                fontSize = 22.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = "Are you sure you want to delete?",
                fontSize = 18.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp)
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirmDelete
            ) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = onCancelDelete
            ) {
                Text(text = "Cancel")
            }
        }
    )
}
