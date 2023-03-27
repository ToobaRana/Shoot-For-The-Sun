package com.example.sunandmoon

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape

//https://gml.noaa.gov/grad/solcalc/solareqns.PDF
//https://thesolarlabs.com/ros/solar-angles/

//https://gml.noaa.gov/grad/solcalc/
//https://gml.noaa.gov/grad/solcalc/calcdetails.html

typealias Degree = Double
typealias Radian = Double
fun Degree.toRadian(): Radian = this / 180 * Math.PI
fun Radian.toDegree(): Degree = this * 180 / Math.PI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MathScreen(modifier: Modifier = Modifier) {
    var solarTimes by remember { mutableStateOf(listOf("")) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {


        Column() {
            Text(text = "Position:", fontSize = 18.sp)
            Text(text = "Latitude:")
            TextField(value = "0", onValueChange = {})
            Text(text = "Longitude:")
            TextField(value = "0", onValueChange = {})
            Button(onClick = { /*TODO*/ }, shape = RectangleShape) {
                Text(text = "Get current position")
            }

            Text(text = "time:")
            TextField(value = "0", onValueChange = {})
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Current Time")
            }

            Button(onClick = { solarTimes = getSunRiseNoonFall() }) {
                Text(text = "Calculate")
            }
        }

        if(solarTimes.size >= 3) {
            Text(text = "Sunrise time:", fontSize = 25.sp)
            Text(text = solarTimes[0], fontSize = 25.sp)

            Spacer(modifier = Modifier.size(40.dp))

            /*Text(text = "Solar noon time:", fontSize = 25.sp)
            Text(text = solarTimes[1], fontSize = 25.sp)*/

            Spacer(modifier = Modifier.size(40.dp))

            Text(text = "Sunset time:", fontSize = 25.sp)
            Text(text = solarTimes[2], fontSize = 25.sp)
        }


        Spacer(modifier = Modifier.size(40.dp))
    }
}

// Calculates and returns a list of sunrise and sunset and sunset (as strings)
fun getSunRiseNoonFall(): List<String> {
    Log.i("matte", "_______________________________________________________")
    // hours from utc
    val timeZoneOffset: Double = 1.0

    val timeString: String = Instant.now().toString().split("T")[1]
    Log.i("matte", "timeString: $timeString")
    val hour: Double = timeString.split(":")[0].toDouble()
    val minutes: Double = timeString.split(":")[1].toDouble()
    val seconds: Double = timeString.split(":")[2].dropLast(1).toDouble()

    // longitude is in degrees (positive to the east of the Prime Meridian)
    val longitude: Degree = 10.7175147
    val latitude: Degree = 59.943621

    val dayOfYear: Double = LocalDate.now().dayOfYear.toDouble() + 31
    val hourDecimal: Double = hour + minutes / 60 + seconds / 60 / 60 + timeZoneOffset
    val isLeapYear: Boolean = false
    Log.i("matte", "dayOfYear: $dayOfYear")
    Log.i("matte", "hourDecimal: $hourDecimal")

    val daysInYear: Double = if(isLeapYear) 366.0 else 365.0
    Log.i("matte", "daysInYear: $daysInYear")

    // this is the fraction of the year. radians
    val y: Radian = (2 * Math.PI / daysInYear) * (dayOfYear - 1 + ((hourDecimal - 12) / 24))
    Log.i("matte", "y: $y")

    // in minutes
    //val eqtime: Double = 229.18 * (0.000075 + 0.001868 * cos(y) + 0.032077 * sin(y) - 0.014615 * cos(2 * y) - 0.040849 * sin(2 * y))
    //val n = 2 * PI / 365 * (day - 1)
    val eqtime = 229.18*(0.000075 + 0.001868* cos(y) - 0.032077*sin(y) -0.014615*cos(2*y) - 0.040849*sin(2* y) )
    Log.i("matte", "eqtime: $eqtime")

    // radians
    //val decl: Radian = 0.006918 - 0.399912 * cos(y) + 0.070257 * sin(y) - 0.006758 * cos(2 * y) + 0.000907 * sin(2 * y) - 0.002697 * cos(3 * y) + 0.00148 * sin(3 * y)
    val decl = 11.55.toRadian()//(90-(Math.toDegrees(acos(sin(Math.toRadians(-23.44)* Math.cos(Math.toRadians((360/365.24)*(dayOfYear+10)+360/Math.PI*0.0167*sin(Math.toRadians((360/365.24)*(dayOfYear-2)))))))))).toRadian()
    Log.i("matte", "decl radian: $decl, decl degrees: ${decl.toDegree()}")

    val timeOffset: Double = eqtime + 4 * longitude - 60 * timeZoneOffset
    Log.i("matte", "timeOffset: $timeOffset")

    val tst: Double = hour*60 + minutes + seconds/60 + timeOffset
    Log.i("matte", "tst: $tst")

    // the solar hour angle in degrees is:
    val ha: Degree = tst / 4 - 180
    Log.i("matte", "ha: $ha")

    // THE FORMULA IN THE PAPER SAYS cost AND NOT cos. IS THIS A SPELLING MISTAKE?
    val zenithAngle: Radian = acos(sin(latitude.toRadian()) * sin(decl) + cos(latitude.toRadian()) * cos(decl) * cos(ha.toRadian()))
    Log.i("matte", "zenithAngle: $zenithAngle")

    val haSunrise: Radian = acos(cos(90.833.toRadian())/(cos(latitude.toRadian())*cos(decl)) - tan(latitude.toRadian()) * tan(decl))
    val haSunset: Radian = -haSunrise
    Log.i("matte", "haSunrise: $haSunrise")
    Log.i("matte", "haSunset: $haSunset")

    val sunriseTime = 720 - 4 * (longitude + haSunrise.toDegree()) - eqtime
    val sunsetTime = 720 - 4 * (longitude + haSunset.toDegree()) - eqtime


    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    val sunriseTimeLocalTime: LocalTime = LocalTime.ofSecondOfDay(((sunriseTime) * 60 + 3600 * timeZoneOffset).toLong())
    System.out.println("sunriseTimeLocalTime: " + sunriseTimeLocalTime.format(formatter));

    val sunsetTimeLocalTime: LocalTime = LocalTime.ofSecondOfDay(((sunsetTime) * 60 + 3600 * timeZoneOffset).toLong())
    System.out.println("sunriseTimeLocalTime: " + sunsetTimeLocalTime.format(formatter))

    return listOf(sunriseTimeLocalTime.format(formatter), sunsetTimeLocalTime.format(formatter), sunsetTimeLocalTime.format(formatter))
}



/*fun getHourDecimal(timestamp: String, timeZoneOffset: Double): Double {
    val time: String = timestamp.split("T")[1]
    val hour: Double = time.split(":")[0].toDouble()
    val minutes: Double = time.split(":")[1].toDouble()
    val seconds: Double = time.split(":")[2].dropLast(1).toDouble()

    return hour + minutes / 60 + seconds / 60 / 60 + timeZoneOffset
}*/