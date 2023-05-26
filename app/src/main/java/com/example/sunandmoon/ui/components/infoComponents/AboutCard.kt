package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R
import com.example.sunandmoon.ui.components.buttonComponents.HyperlinkText

//card to display info about the app
@Composable
fun AboutCard(modifier: Modifier) {

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(30.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            item{
                Text(
                text = stringResource(id = R.string.AppInfo),
                fontSize = 30.sp,
                modifier = modifier,
                textAlign = TextAlign.Center
                )
            }
            item{Text(text = stringResource(id = R.string.AppDescription))}
            item{Text(text = stringResource(id = R.string.AR))}
            item{Text(text = stringResource(id = R.string.ARDescription))}
            item{Text(text = stringResource(id = R.string.Planning))}
            item{Text(text = stringResource(id = R.string.TableDescription))}
            item{HyperlinkText(
                fullText = stringResource(id = R.string.IconDescription),
                linkText = listOf(stringResource(id = R.string.Icons8), stringResource(id = R.string.carlosyllobre)),
                hyperlinks = listOf("https://icons8.com/", "https://www.figma.com/community/file/955978734883254712/Weather-Icons-%7C-Flat-%26-Outline"),
                linkTextColor = MaterialTheme.colorScheme.background,
                fontSize = 18.sp)
            }
            item{Text(text = "${stringResource(id = R.string.Disclaimer)}\n${ stringResource(R.string.Disclaimertext)}")}


        }

    }
}