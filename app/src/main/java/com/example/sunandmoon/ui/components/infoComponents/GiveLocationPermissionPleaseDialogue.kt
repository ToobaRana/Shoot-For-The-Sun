package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunandmoon.R
import com.example.sunandmoon.viewModel.ARViewModel

//alerts the user that location is required via a popup
@Composable
fun GiveLocationPermissionPleaseDialogue(modifier: Modifier, goBack: ()->Unit) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.primary,
        onDismissRequest = {
            goBack()
        },
        text = {
            Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

                Text(text =
                    stringResource(id = R.string.PleaseGiveLocationPermission),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.nunito_bold))
                )

                Icon(
                    painter = painterResource(R.drawable.location1),
                    stringResource(id = R.string.PleaseGiveLocationPermission),
                    modifier.size(120.dp),
                )
            }
        },
        confirmButton = {
            Button(onClick = { goBack() }) {
                Text(text = stringResource(id = R.string.OK), fontFamily = FontFamily(Font(R.font.nunito_bold)))
            }
        }
    )
}