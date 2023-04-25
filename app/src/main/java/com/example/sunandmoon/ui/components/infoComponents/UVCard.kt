package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R

@Composable
fun UVCard(modifier: Modifier){

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {

        Column(
            modifier = modifier
                .padding(15.dp)
        ) {

            Text(text = "12:15")

            Row(
                modifier
                    .padding(start = 60.dp, top = 5.dp)
                    .fillMaxWidth()
            ){

                //UV icon that symbolises how intense the UV-rays are
                Image(
                    painter = painterResource(id = R.drawable.uv1),
                    contentDescription = "UV-intensity based icon",
                    modifier = modifier
                        .size(80.dp)
                )

                Spacer(modifier.size(40.dp))

                Column(
                ){

                    //UV info
                    Text(text = "UV-index: 1", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier.size(10.dp))
                    Text(text = "No need for protection", fontSize = 15.sp)
                }
            }
        }
    }
}