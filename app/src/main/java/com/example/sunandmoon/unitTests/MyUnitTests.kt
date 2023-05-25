package com.example.sunandmoon.unitTests

import android.location.Location
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sunandmoon.getSunRiseNoonFall
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.LocalTime

class DemoTests {

    @Test
    fun hmm() {
        val a = 2
        assert(2 == a)

        assert(ahh())
    }
}

fun ahh(): Boolean {
    val a = Location("").apply {
        latitude = 10.0
        longitude = 10.0
    }
    return true
}


/*val uio = Location("").apply {
    latitude = 59.940524
    longitude = 10.7217161
}
val brasilia = Location("").apply {
    latitude = -15.78772
    longitude = -47.924079
}
val capeTown = Location("").apply {
    latitude = -33.92415
    longitude = 18.4264587
}
val sanFrancisco = Location("").apply {
    latitude = 37.770023
    longitude = -122.42603
}
val beijing = Location("").apply {
    latitude = 39.906172
    longitude = 116.4111
}

val testDate1 = LocalDateTime.of(2023, 3, 10, 20, 30)
val testDate2 = LocalDateTime.of(2023, 5, 1, 12, 0)
val testDate3 = LocalDateTime.of(2015, 1, 14, 8, 0)
val testDate4 = LocalDateTime.of(2029, 12, 30, 23, 0)
val testDate5 = LocalDateTime.of(2024, 7, 4, 1, 0)


class UnitTests {

    @Test
    fun testSunriseCalculations() {
        val a = actuallyTestSunriseCalculations()
        assert(a)

    }

    @Test
    fun testSunsetCalculations() {

        val uioExpectedSunsetDate1 = LocalTime.of(19, 6)
        val uioExpectedSunsetDate3 = LocalTime.of(16, 46)
        val uioExpectedSunsetDate5 = LocalTime.of(0, 0)

        val brasiliaExpectedSunsetDate2 = LocalTime.of(0, 0)
        val brasiliaExpectedSunsetDate4 = LocalTime.of(0, 0)

        val capeTownExpectedSunsetDate1 = LocalTime.of(0, 0)
        val capeTownExpectedSunsetDate4 = LocalTime.of(0, 0)

        val sanFranciscoExpectedSunsetDate2 = LocalTime.of(0, 0)
        val sanFranciscoExpectedSunsetDate5 = LocalTime.of(0, 0)

        val beijingExpectedSunsetDate3 = LocalTime.of(0, 0)
        val beijingExpectedSunsetDate1 = LocalTime.of(0, 0)

    }

    @Test
    fun testSolarNoonCalculations() {

        val uioExpectedSunsetDate1 = LocalTime.of(13, 27)
        val uioExpectedSunsetDate3 = LocalTime.of(13, 26)
        val uioExpectedSunsetDate5 = LocalTime.of(0, 0)

        val brasiliaExpectedSunsetDate2 = LocalTime.of(0, 0)
        val brasiliaExpectedSunsetDate4 = LocalTime.of(0, 0)

        val capeTownExpectedSunsetDate1 = LocalTime.of(0, 0)
        val capeTownExpectedSunsetDate4 = LocalTime.of(0, 0)

        val sanFranciscoExpectedSunsetDate2 = LocalTime.of(0, 0)
        val sanFranciscoExpectedSunsetDate5 = LocalTime.of(0, 0)

        val beijingExpectedSunsetDate3 = LocalTime.of(0, 0)
        val beijingExpectedSunsetDate1 = LocalTime.of(0, 0)

    }
}

fun checkIfInMaxAllowedMinuteMargin(expectedTime: LocalTime, timeToTest: LocalTime): Boolean {
    val minutesMargin: Long = 5
    if(expectedTime.isAfter(timeToTest.plusMinutes(minutesMargin))) return false
    if(expectedTime.isBefore(timeToTest.minusMinutes(minutesMargin))) return false
    return true
}

fun actuallyTestSunriseCalculations():Boolean {
    val uioExpectedSunriseDate1 = LocalTime.of(7, 50)
    val uioExpectedSunriseDate3 = LocalTime.of(6, 2)
    val uioExpectedSunriseDate5 = LocalTime.of(0, 0)

    val brasiliaExpectedSunriseDate2 = LocalTime.of(0, 0)
    val brasiliaExpectedSunriseDate4 = LocalTime.of(0, 0)

    val capeTownExpectedSunriseDate1 = LocalTime.of(0, 0)
    val capeTownExpectedSunriseDate4 = LocalTime.of(0, 0)

    val sanFranciscoExpectedSunriseDate2 = LocalTime.of(0, 0)
    val sanFranciscoExpectedSunriseDate5 = LocalTime.of(0, 0)

    val beijingExpectedSunriseDate3 = LocalTime.of(0, 0)
    val beijingExpectedSunriseDate1 = LocalTime.of(0, 0)

    //println(getSunRiseNoonFall(testDate1, 1.0, uio)[0].toString())

    if(!checkIfInMaxAllowedMinuteMargin(
        uioExpectedSunriseDate1,
        getSunRiseNoonFall(testDate1, 1.0, uio)[0]
    )) return false
    if(!checkIfInMaxAllowedMinuteMargin(
        uioExpectedSunriseDate3,
        getSunRiseNoonFall(testDate3, 0.0, uio)[0]
    )) return false
    if(!checkIfInMaxAllowedMinuteMargin(
        uioExpectedSunriseDate5,
        getSunRiseNoonFall(testDate5, 0.0, uio)[0]
    )) return false

    if(!checkIfInMaxAllowedMinuteMargin(
        brasiliaExpectedSunriseDate2,
        getSunRiseNoonFall(testDate2, 0.0, brasilia)[0]
    )) return false
    if(!checkIfInMaxAllowedMinuteMargin(
        brasiliaExpectedSunriseDate4,
        getSunRiseNoonFall(testDate4, 0.0, brasilia)[0]
    )) return false

    if(!checkIfInMaxAllowedMinuteMargin(
        capeTownExpectedSunriseDate1,
        getSunRiseNoonFall(testDate1, 0.0, capeTown)[0]
    )) return false
    if(!checkIfInMaxAllowedMinuteMargin(
        capeTownExpectedSunriseDate4,
        getSunRiseNoonFall(testDate4, 0.0, capeTown)[0]
    )) return false

    return true
}*/