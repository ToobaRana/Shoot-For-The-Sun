package com.example.sunandmoon.ui.components.infoComponents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sunandmoon.R
import com.example.sunandmoon.ui.theme.*
import java.time.LocalTime

//used for displaying uv-info in shootInfoScreen
@Composable
fun UVCard(modifier: Modifier, time: LocalTime, uvIndex : Double?) {
    val uvColor = when(uvIndex?.toInt()){
        in 0..2 -> UVLowColor
        in 3..5 -> UVModerateColor
        in 6..7 -> UVHighColor
        in 8..10 -> UVVeryHighColor
        in 11.. Int.MAX_VALUE-> UVExtremeColor
        else -> SecondaryColor
    }

    val uvDescriptionsArray: Array<String> = stringArrayResource(R.array.uv_description)

    val uvDescription = when(uvIndex?.toInt()){
        in 0..2 -> uvDescriptionsArray[0]
        in 3..5 -> uvDescriptionsArray[1]
        in 6..7 -> uvDescriptionsArray[2]
        in 8..10 -> uvDescriptionsArray[3]
        in 11.. Int.MAX_VALUE-> uvDescriptionsArray[4]
        else -> uvDescriptionsArray[5]
    }
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

        Box(
            modifier = modifier
                .padding(15.dp)
        ) {

            Text(text = time.toString(), fontSize = 18.sp)

            Row(
                modifier
                    .padding(start = 40.dp, top = 30.dp, end = 5.dp)
                    .fillMaxWidth()
            ) {

                Spacer(modifier.size(10.dp))


                //UV icon that symbolises how intense the UV-rays are
                Icon(
                    painter = painterResource(id = R.drawable.sun),
                    stringResource(id = R.string.UVIntensityImage),
                    modifier.size(80.dp),
                    uvColor
                )


                Spacer(modifier.size(30.dp))

                Column{

                    //UV info
                    Text(text = stringResource(id = R.string.UVIndex) + ": " + uvIndex.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier.size(10.dp))
                    Text(text = uvDescription, fontSize = 18.sp)
                }
            }
        }
    }
}