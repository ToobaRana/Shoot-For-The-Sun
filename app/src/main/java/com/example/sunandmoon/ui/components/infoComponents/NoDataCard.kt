package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R


//card for displaying that no data is available
@Composable
fun NoDataCard(modifier : Modifier, missingNetworkConnection: Boolean){
    val cardText : String =
        if(missingNetworkConnection) stringResource(R.string.NoInternet)
        else stringResource(R.string.NoData)

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp)
            .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(modifier = modifier.align(CenterHorizontally).fillMaxHeight(0.9f), verticalArrangement = Arrangement.SpaceAround) {
            Text(text = cardText, textAlign = TextAlign.Center, lineHeight = 30.sp)
        }

    }
}