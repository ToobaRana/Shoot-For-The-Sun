package com.example.sunandmoon.ui.components.buttonComponents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R

//used to delete shoot or production
@Composable
fun DeleteButton(
    onConfirmDelete: () -> Unit,
    onCancelDelete: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancelDelete,
        title = {
            Text(
                text = stringResource(id = R.string.Delete),
                fontSize = 22.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.DeleteBoxText),
                fontSize = 18.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                modifier = Modifier.padding(top = 10.dp)
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirmDelete
            ) {
                Text(text = stringResource(id = R.string.Confirm), fontFamily = FontFamily(Font(R.font.nunito_bold)))
            }
        },
        dismissButton = {
            Button(
                onClick = onCancelDelete
            ) {
                Text(text = stringResource(id = R.string.Cancel), fontFamily = FontFamily(Font(R.font.nunito_bold)))
            }
        }
    )
}
