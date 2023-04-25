package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R

@Composable
fun WindCard(modifier: Modifier){

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

                Column(
                ){
                    //The wind description
                    Image(painter = painterResource(id = R.drawable.wind), contentDescription = "wind", modifier = modifier.size(50.dp))
                    Text(text = "4 m/s",fontSize = 20.sp)
                }

                Spacer(modifier.size(80.dp))

                //pic of wind compass
                Image(
                    painter = painterResource(id = R.drawable.windcompass),
                    contentDescription = "wind compass",
                    modifier = modifier
                        .size(115.dp)
                )

            }
        }
    }
}