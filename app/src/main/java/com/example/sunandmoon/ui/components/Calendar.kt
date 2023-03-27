package com.example.sunandmoon.ui.components

import android.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek

class Calendar(private val modifier: Modifier) {
    val calenderDayHeight = 5
    val numWeekdays = 6
    val sumMonths = 12
    val months = listOf<String>(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )

    val amountDays: HashMap<String, Int> = hashMapOf<String, Int>(
        "January" to 31,
        "February" to 28,
        "March" to 31 ,
        "April" to 30,
        "May" to 31,
        "June" to 30,
        "July" to 31,
        "August" to 31,
        "September" to 30,
        "October" to 31,
        "November" to 30,
        "December" to 31
    )
    val weekdays = listOf<String>("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CalendarComponent(){
        Card(modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(0.4f)){

            Column(modifier = Modifier.fillMaxSize()){
                Row(modifier = Modifier.fillMaxWidth().height(40.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically){
                    Box(){}
                    Box(){Text(text = "Month")}
                    Box(){Text(text = "year")}
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically){
                    for (y in 0 until numWeekdays) {
                        Text(text = weekdays[y])
                    }

                }
                for (i in 0..calenderDayHeight){
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically){

                            for (y in 0 until numWeekdays) {
                                Text(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(1.dp)
                                        .clickable { },
                                    text = (y + (i  * 7) + 1).toString(),
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,

                                )


                            }


                    }
                }
            }

        }
    }

}