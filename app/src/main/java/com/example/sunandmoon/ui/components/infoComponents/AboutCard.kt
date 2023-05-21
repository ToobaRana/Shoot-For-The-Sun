package com.example.sunandmoon.ui.components.infoComponents

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R
import com.example.sunandmoon.ui.components.buttonComponents.HyperlinkText


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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(30.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Text(
                text = stringResource(id = R.string.AppInfo),
                fontSize = 30.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(text = stringResource(id = R.string.AppDescription))
            Text(text = stringResource(id = R.string.ARDescription))
            Text(text = stringResource(id = R.string.TableDescription))



            HyperlinkText(

                fullText = stringResource(id = R.string.IconDescription),
                linkText = listOf(stringResource(id = R.string.Icons8), stringResource(id = R.string.carlosyllobre)),
                hyperlinks = listOf("https://icons8.com/", "https://www.figma.com/community/file/955978734883254712/Weather-Icons-%7C-Flat-%26-Outline"),
                linkTextColor = MaterialTheme.colorScheme.background,
                fontSize = 18.sp
            )


        }

    }
}