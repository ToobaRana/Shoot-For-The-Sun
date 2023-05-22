package com.example.sunandmoon.ui.components.buttonComponents

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R
import com.example.sunandmoon.viewModel.ProductionSelectionViewModel

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
                text = "Deleting",
                fontSize = 28.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = "Are you sure you want to delete this?",
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
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
