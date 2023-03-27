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
            Button(onClick = { solarTimes = getSunRiseNoonFall(Instant.now().toString(), 10.7175147, 59.943621) }) {
                Text(text = "Calculate")
            }
        }

        if(solarTimes.size >= 3) {
            Text(text = "Sunrise time:", fontSize = 25.sp)
            Text(text = solarTimes[0], fontSize = 25.sp)

            Spacer(modifier = Modifier.size(40.dp))

            Text(text = "Solar noon time:", fontSize = 25.sp)
            Text(text = solarTimes[1], fontSize = 25.sp)

            Spacer(modifier = Modifier.size(40.dp))

            Text(text = "Sunset time:", fontSize = 25.sp)
            Text(text = solarTimes[2], fontSize = 25.sp)
        }


        Spacer(modifier = Modifier.size(40.dp))
    }
}

// Calculates and returns a list of sunrise and sunset and sunset (as strings)
// longitude is in degrees (positive to the east of the Prime Meridian)
fun getSunRiseNoonFall(timestampString: String, longitude: Degree, latitude: Degree): List<String> {
    // hours from utc
    val timeZoneOffset: Double = 2.0

    val timeString: String = timestampString.split("T")[1]
    val hour: Double = timeString.split(":")[0].toDouble()
    val minutes: Double = timeString.split(":")[1].toDouble()
    val seconds: Double = timeString.split(":")[2].dropLast(1).toDouble()

    val dateString: List<String> = timestampString.split("T")[0].split("-")
    val dayOfYear: Double = LocalDate.of(dateString[0].toInt(), dateString[1].toInt(), dateString[2].toInt()).dayOfYear.toDouble()
    val hourDecimal: Double = hour + minutes / 60 + seconds / 60 / 60 + timeZoneOffset
    val isLeapYear: Boolean = false

    val daysInYear: Double = if(isLeapYear) 366.0 else 365.0

    // this is the fraction of the year. radians
    val y: Radian = (2 * Math.PI / daysInYear) * (dayOfYear - 1 + ((hourDecimal - 12) / 24))

    // in minutes
    //val eqtime: Double = 229.18 * (0.000075 + 0.001868 * cos(y) + 0.032077 * sin(y) - 0.014615 * cos(2 * y) - 0.040849 * sin(2 * y))
    //val n = 2 * PI / 365 * (day - 1)
    val eqtime = 229.18*(0.000075 + 0.001868* cos(y) - 0.032077*sin(y) -0.014615*cos(2*y) - 0.040849*sin(2* y) )

    // radians
    //val decl: Radian = 0.006918 - 0.399912 * cos(y) + 0.070257 * sin(y) - 0.006758 * cos(2 * y) + 0.000907 * sin(2 * y) - 0.002697 * cos(3 * y) + 0.00148 * sin(3 * y)
    val decl = (90-(Math.toDegrees(acos(sin(Math.toRadians(-23.44)* Math.cos(Math.toRadians((360/365.24)*(dayOfYear+10)+360/Math.PI*0.0167*sin(Math.toRadians((360/365.24)*(dayOfYear-2)))))))))).toRadian()

    val timeOffset: Double = eqtime + 4 * longitude - 60 * timeZoneOffset

    val tst: Double = hour*60 + minutes + seconds/60 + timeOffset

    // the solar hour angle in degrees is:
    val ha: Degree = tst / 4 - 180

    // THE FORMULA IN THE PAPER SAYS cost AND NOT cos. IS THIS A SPELLING MISTAKE?
    val zenithAngle: Radian = acos(sin(latitude.toRadian()) * sin(decl) + cos(latitude.toRadian()) * cos(decl) * cos(ha.toRadian()))

    val haSunrise: Radian = acos(cos(90.833.toRadian())/(cos(latitude.toRadian())*cos(decl)) - tan(latitude.toRadian()) * tan(decl))
    val haSunset: Radian = -haSunrise

    val sunriseTime = 720 - 4 * (longitude + haSunrise.toDegree()) - eqtime
    val sunsetTime = 720 - 4 * (longitude + haSunset.toDegree()) - eqtime
    val solarNoonTime = 720 - 4 * longitude - eqtime


    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val sunriseTimeLocalTime: LocalTime = LocalTime.ofSecondOfDay(((sunriseTime) * 60 + 3600 * timeZoneOffset).toLong())
    val sunsetTimeLocalTime: LocalTime = LocalTime.ofSecondOfDay(((sunsetTime) * 60 + 3600 * timeZoneOffset).toLong())
    val solarNoonTimeLocalTime: LocalTime = LocalTime.ofSecondOfDay(((solarNoonTime) * 60 + 3600 * timeZoneOffset).toLong())


    Log.i("matte", "_______________________________________________________")
    Log.i("matte", timestampString)
    Log.i("matte", "timeString: $timeString")
    Log.i("matte", "dayOfYear: $dayOfYear")
    Log.i("matte", "hourDecimal: $hourDecimal")
    Log.i("matte", "daysInYear: $daysInYear")
    Log.i("matte", "y: $y")
    Log.i("matte", "eqtime: $eqtime")
    Log.i("matte", "decl radian: $decl, decl degrees: ${decl.toDegree()}")
    Log.i("matte", "timeOffset: $timeOffset")
    Log.i("matte", "tst: $tst")
    Log.i("matte", "ha: $ha")
    Log.i("matte", "zenithAngle: $zenithAngle")
    Log.i("matte", "haSunrise: $haSunrise")
    Log.i("matte", "haSunset: $haSunset")
    Log.i("matte", "sunriseTimeLocalTime: " + sunriseTimeLocalTime.format(formatter))
    Log.i("matte", "solarNoonTimeLocalTime: " + solarNoonTimeLocalTime.format(formatter))
    Log.i("matte", "sunriseTimeLocalTime: " + sunsetTimeLocalTime.format(formatter))
    Log.i("matte", "_______________________________________________________")


    return listOf(sunriseTimeLocalTime.format(formatter), solarNoonTimeLocalTime.format(formatter), sunsetTimeLocalTime.format(formatter))
}