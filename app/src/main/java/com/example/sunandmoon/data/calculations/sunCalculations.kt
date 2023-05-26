package com.example.sunandmoon.data.calculations

import android.location.Location
import android.util.Log
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.*

//https://gml.noaa.gov/grad/solcalc/solareqns.PDF
//https://thesolarlabs.com/ros/solar-angles/

//https://gml.noaa.gov/grad/solcalc/
//https://gml.noaa.gov/grad/solcalc/calcdetails.html

//defines the type of a variable for keeping track easily
typealias Degree = Double
typealias Radian = Double
fun Degree.toRadian(): Radian = this / 180 * Math.PI
fun Radian.toDegree(): Degree = this * 180 / Math.PI

// Calculates and returns a list of sunrise and solar noon and sunset (as LocalTime objects)
fun getSunRiseNoonFall(localDateTime: LocalDateTime, timeZoneOffset: Double, location: Location): List<LocalTime> {

    val latitude: Degree =  location.latitude
    val longitude: Degree = location.longitude

    // in minutes
    val eqtime: Double = calculateEquationOfTime(localDateTime, timeZoneOffset)

    // radians
    val decl: Radian = calculateDeclinationAngle(localDateTime)

    val haSunrise: Radian = acos(cos(90.833.toRadian())/(cos(latitude.toRadian())*cos(decl)) - tan(latitude.toRadian()) * tan(decl))
    val haSunset: Radian = -haSunrise

    val sunriseTime = 720 - 4 * (longitude + haSunrise.toDegree()) - eqtime
    val sunsetTime = 720 - 4 * (longitude + haSunset.toDegree()) - eqtime
    val solarNoonTime = 720 - 4 * longitude - eqtime


    //val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    // the try-cathes are to take care of when the sun never raises or sets
    // (for some parts of the year far north or south)
    val sunriseTimeLocalTime = try{
        LocalTime.ofSecondOfDay(((sunriseTime) * 60 + 3600 * timeZoneOffset).toLong())
    } catch (_: Exception) {
        LocalTime.of(0,0)
    }
    val sunsetTimeLocalTime = try{
        LocalTime.ofSecondOfDay(((sunsetTime) * 60 + 3600 * timeZoneOffset).toLong())
    } catch (_: Exception) {
        LocalTime.of(0,0)
    }
    val solarNoonTimeLocalTime = try{
        LocalTime.ofSecondOfDay(((solarNoonTime) * 60 + 3600 * timeZoneOffset).toLong())
    } catch (_: Exception) {
        LocalTime.of(0,0)
    }


    /*val sunriseTimeLocalTimeRounded = roundToNearestMinute(sunriseTimeLocalTime.format(formatter))
    val sunsetTimeLocalTimeRounded = roundToNearestMinute(solarNoonTimeLocalTime.format(formatter))
    val solarNoonTimeLocalTimeRounded = roundToNearestMinute(sunsetTimeLocalTime.format(formatter))*/

    Log.i("matte", "_______________________________________________________")
    Log.i("matte", localDateTime.toString())
    Log.i("matte", "timeZoneOffset $timeZoneOffset")
    Log.i("matte", "latitude: $latitude")
    Log.i("matte", "longitude: $longitude")
    Log.i("matte", "eqtime: $eqtime")
    Log.i("matte", "decl radian: $decl, decl degrees: ${decl.toDegree()}")
    Log.i("matte", "haSunrise: $haSunrise")
    Log.i("matte", "haSunset: $haSunset")
    Log.i("matte", "sunriseTimeLocalTime: $sunriseTimeLocalTime")
    Log.i("matte", "solarNoonTimeLocalTime: $solarNoonTimeLocalTime")
    Log.i("matte", "sunriseTimeLocalTime: $sunsetTimeLocalTime")
    Log.i("matte", "_______________________________________________________")

    return listOf(roundToNearestMinute(sunriseTimeLocalTime), roundToNearestMinute(solarNoonTimeLocalTime), roundToNearestMinute(sunsetTimeLocalTime))
}

//returns hour decimal variable in double
fun calculateHourDecimal(localDateTime: LocalDateTime, timeZoneOffset: Double): Double {
    val hour: Double = localDateTime.hour.toDouble()
    val minutes: Double = localDateTime.minute.toDouble()
    val seconds: Double = localDateTime.second.toDouble()

    return hour + minutes / 60 + seconds / 60 / 60 + timeZoneOffset
}

//return fraction of year variable in radians
fun calculateFractionOfYear(localDateTime: LocalDateTime, timeZoneOffset: Double, dayOfYear: Double): Radian {
    val hourDecimal: Double = calculateHourDecimal(localDateTime, timeZoneOffset)
    val isLeapYear: Boolean = (localDateTime.toLocalDate().year % 4) == 0

    val daysInYear: Double = if(isLeapYear) 366.0 else 365.0

    Log.i("matte", "hourDecimal: $hourDecimal")
    Log.i("matte", "daysInYear: $daysInYear")

    // this is the fraction of the year. radians
    return (2 * Math.PI / daysInYear) * (dayOfYear - 1 + ((hourDecimal - 12) / 24))
}

// returns equation of time in minutes
fun calculateEquationOfTime(localDateTime: LocalDateTime, timeZoneOffset: Double): Double {
    val dayOfYear: Double = localDateTime.toLocalDate().dayOfYear.toDouble()

    // this is the fraction of the year. radians
    val y: Radian = calculateFractionOfYear(localDateTime, timeZoneOffset, dayOfYear)

    Log.i("matte", "dayOfYear: $dayOfYear")
    Log.i("matte", "y: $y")

    // in minutes
    //val eqtime: Double = 229.18 * (0.000075 + 0.001868 * cos(y) + 0.032077 * sin(y) - 0.014615 * cos(2 * y) - 0.040849 * sin(2 * y))
    //val n = 2 * PI / 365 * (day - 1)
    return 229.18*(0.000075 + 0.001868* cos(y) - 0.032077*sin(y) -0.014615*cos(2*y) - 0.040849*sin(2* y) )
}

fun calculateDeclinationAngle(localDateTime: LocalDateTime): Radian {
    val dayOfYear: Double = localDateTime.toLocalDate().dayOfYear.toDouble()

    // radians
    //val decl: Radian = 0.006918 - 0.399912 * cos(y) + 0.070257 * sin(y) - 0.006758 * cos(2 * y) + 0.000907 * sin(2 * y) - 0.002697 * cos(3 * y) + 0.00148 * sin(3 * y)
    return (90-(Math.toDegrees(acos(sin(Math.toRadians(-23.44)* cos(Math.toRadians((360/365.24)*(dayOfYear+10)+360/Math.PI*0.0167*sin(Math.toRadians((360/365.24)*(dayOfYear-2)))))))))).toRadian()
}

// for the AR feature. Returns the sun azimuth angle and zenith angles, both in degrees
fun calculateSunPosition(localDateTime: LocalDateTime, timeZoneOffset: Double, location: Location): Pair<Double, Double> {

    val latitude: Degree =  location.latitude
    val longitude: Degree = location.longitude

    // in minutes
    val eqtime: Double = calculateEquationOfTime(localDateTime, timeZoneOffset)

    // radians
    val decl: Radian = calculateDeclinationAngle(localDateTime)

    val hour: Double = localDateTime.hour.toDouble()
    val minutes: Double = localDateTime.minute.toDouble()
    val seconds: Double = localDateTime.second.toDouble()

    val timeOffset: Double = eqtime + 4 * longitude - 60 * timeZoneOffset

    val tst: Double = hour*60 + minutes + seconds/60 + timeOffset

    // the solar hour angle in degrees is:
    val ha: Degree = tst / 4 - 180

    // THE FORMULA IN THE PAPER SAYS cost AND NOT cos. IS THIS A SPELLING MISTAKE?
    val zenithAngle: Radian = (acos(sin(latitude.toRadian()) * sin(decl) + cos(latitude.toRadian()) * cos(decl) * cos(ha.toRadian())) - Math.PI / 2 ) * -1

    // Degrees clockwise from north
    /*val azimuthAngle: Degree =
        -(acos(-(
            (sin(latitude.toRadian()) * cos(zenithAngle) - sin(decl)) /
            (cos(latitude.toRadian()) * sin(zenithAngle))
        )) - 180)*/

    val azimuthAngle: Degree = (atan2(sin(ha.toRadian()), cos(ha.toRadian()) * sin(latitude.toRadian()) - tan(decl) * cos(latitude.toRadian()))).toDegree() + 180

    Log.i("matte", localDateTime.toString())
    Log.i("matte", "timeZoneOffset $timeZoneOffset")
    Log.i("matte", "latitude: $latitude")
    Log.i("matte", "longitude: $longitude")
    Log.i("matte", "eqtime: $eqtime")
    Log.i("matte", "decl radian: $decl, decl degrees: ${decl.toDegree()}")

    Log.i("matte", "timeOffset: $timeOffset")
    Log.i("matte", "tst: $tst")
    Log.i("matte", "ha: $ha")
    Log.i("matte", "zenithAngle.toDegree(): ${zenithAngle.toDegree()}")
    Log.i("matte", "azimuthAngle: $azimuthAngle")

    return Pair(azimuthAngle, zenithAngle.toDegree())
}


fun roundToNearestMinute(time: LocalTime): LocalTime {
    return time.truncatedTo(ChronoUnit.MINUTES)
}