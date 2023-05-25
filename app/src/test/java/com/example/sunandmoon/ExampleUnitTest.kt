package com.example.sunandmoon

import android.location.Location
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.time.LocalDateTime
import java.time.LocalTime
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
val uio = mock(Location::class.java)
val brasilia = mock(Location::class.java)
val capeTown = mock(Location::class.java)
val sanFrancisco = mock(Location::class.java)
val beijing = mock(Location::class.java)


val testDate1 = LocalDateTime.of(2023, 3, 10, 12, 0)
val testDate2 = LocalDateTime.of(2023, 5, 1, 12, 0)
val testDate3 = LocalDateTime.of(2015, 1, 14, 8, 0)
val testDate4 = LocalDateTime.of(2029, 12, 31, 23, 0)
val testDate5 = LocalDateTime.of(2024, 7, 4, 1, 0)

class UnitTests {

    @Before
    fun createLocations() {
        `when`(uio.latitude).thenReturn(59.940524)
        `when`(uio.longitude).thenReturn(10.7217161)

        `when`(brasilia.latitude).thenReturn(-15.78772)
        `when`(brasilia.longitude).thenReturn(-47.924079)

        `when`(capeTown.latitude).thenReturn(-33.92415)
        `when`(capeTown.longitude).thenReturn(18.4264587)

        `when`(sanFrancisco.latitude).thenReturn(37.770023)
        `when`(sanFrancisco.longitude).thenReturn(-122.42603)

        `when`(beijing.latitude).thenReturn(39.906172)
        `when`(beijing.longitude).thenReturn(116.4111)
    }

    @Test
    fun testSunriseCalculations() {

        val uioExpectedSunriseDate1 = LocalTime.of(6, 50)
        val uioExpectedSunriseDate3 = LocalTime.of(9, 6)
        val uioExpectedSunriseDate5 = LocalTime.of(4, 4)

        val brasiliaExpectedSunriseDate2 = LocalTime.of(6, 22)
        val brasiliaExpectedSunriseDate4 = LocalTime.of(6, 43)

        val capeTownExpectedSunriseDate1 = LocalTime.of(6, 41)
        val capeTownExpectedSunriseDate4 = LocalTime.of(5, 38)

        val sanFranciscoExpectedSunriseDate2 = LocalTime.of(6, 14)
        val sanFranciscoExpectedSunriseDate5 = LocalTime.of(5, 53)

        val beijingExpectedSunriseDate1 = LocalTime.of(6, 34)
        val beijingExpectedSunriseDate3 = LocalTime.of(7, 34)


        assert(checkIfInMaxAllowedMinuteMargin(
                uioExpectedSunriseDate1,
                getSunRiseNoonFall(testDate1, 1.0, uio)[0]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
                uioExpectedSunriseDate3,
                getSunRiseNoonFall(testDate3, 1.0, uio)[0]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
                uioExpectedSunriseDate5,
                getSunRiseNoonFall(testDate5, 2.0, uio)[0]
        ))

        assert(checkIfInMaxAllowedMinuteMargin(
                brasiliaExpectedSunriseDate2,
                getSunRiseNoonFall(testDate2, -3.0, brasilia)[0]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
                brasiliaExpectedSunriseDate4,
                getSunRiseNoonFall(testDate4, -2.0, brasilia)[0]
        ))

        assert(checkIfInMaxAllowedMinuteMargin(
                capeTownExpectedSunriseDate1,
                getSunRiseNoonFall(testDate1, 2.0, capeTown)[0]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
                capeTownExpectedSunriseDate4,
                getSunRiseNoonFall(testDate4, 2.0, capeTown)[0]
        ))

        assert(checkIfInMaxAllowedMinuteMargin(
            sanFranciscoExpectedSunriseDate2,
            getSunRiseNoonFall(testDate2, -7.0, sanFrancisco)[0]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
            sanFranciscoExpectedSunriseDate5,
            getSunRiseNoonFall(testDate5, -7.0, sanFrancisco)[0]
        ))

        assert(checkIfInMaxAllowedMinuteMargin(
            beijingExpectedSunriseDate1,
            getSunRiseNoonFall(testDate1, 8.0, beijing)[0]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
            beijingExpectedSunriseDate3,
            getSunRiseNoonFall(testDate3, 8.0, beijing)[0]
        ))

    }

    @Test
    fun testSunsetCalculations() {

        val uioExpectedSunsetDate1 = LocalTime.of(18, 6)
        val uioExpectedSunsetDate3 = LocalTime.of(15, 46)
        val uioExpectedSunsetDate5 = LocalTime.of(22, 38)

        val brasiliaExpectedSunsetDate2 = LocalTime.of(17, 54)
        val brasiliaExpectedSunsetDate4 = LocalTime.of(19, 46)

        val capeTownExpectedSunsetDate1 = LocalTime.of(19, 11)
        val capeTownExpectedSunsetDate4 = LocalTime.of(20, 0)

        val sanFranciscoExpectedSunsetDate2 = LocalTime.of(19, 59)
        val sanFranciscoExpectedSunsetDate5 = LocalTime.of(20, 34)

        val beijingExpectedSunsetDate1 = LocalTime.of(18, 15)
        val beijingExpectedSunsetDate3 = LocalTime.of(17, 12)

        assert(checkIfInMaxAllowedMinuteMargin(
            uioExpectedSunsetDate1,
            getSunRiseNoonFall(testDate1, 1.0, uio)[2]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
            uioExpectedSunsetDate3,
            getSunRiseNoonFall(testDate3, 1.0, uio)[2]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
            uioExpectedSunsetDate5,
            getSunRiseNoonFall(testDate5, 2.0, uio)[2]
        ))

        assert(checkIfInMaxAllowedMinuteMargin(
            brasiliaExpectedSunsetDate2,
            getSunRiseNoonFall(testDate2, -3.0, brasilia)[2]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
            brasiliaExpectedSunsetDate4,
            getSunRiseNoonFall(testDate4, -2.0, brasilia)[2]
        ))

        assert(checkIfInMaxAllowedMinuteMargin(
            capeTownExpectedSunsetDate1,
            getSunRiseNoonFall(testDate1, 2.0, capeTown)[2]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
            capeTownExpectedSunsetDate4,
            getSunRiseNoonFall(testDate4, 2.0, capeTown)[2]
        ))

        assert(checkIfInMaxAllowedMinuteMargin(
            sanFranciscoExpectedSunsetDate2,
            getSunRiseNoonFall(testDate2, -7.0, sanFrancisco)[2]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
            sanFranciscoExpectedSunsetDate5,
            getSunRiseNoonFall(testDate5, -7.0, sanFrancisco)[2]
        ))

        assert(checkIfInMaxAllowedMinuteMargin(
            beijingExpectedSunsetDate1,
            getSunRiseNoonFall(testDate1, 8.0, beijing)[2]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
            beijingExpectedSunsetDate3,
            getSunRiseNoonFall(testDate3, 8.0, beijing)[2]
        ))

    }

    @Test
    fun testSolarNoonCalculations() {

        val uioExpectedSolarNoonDate1 = LocalTime.of(12, 27)
        val uioExpectedSolarNoonDate3 = LocalTime.of(12, 26)
        val uioExpectedSolarNoonDate5 = LocalTime.of(13, 21)

        val brasiliaExpectedSolarNoonDate2 = LocalTime.of(12, 8)
        val brasiliaExpectedSolarNoonDate4 = LocalTime.of(13, 14)

        val capeTownExpectedSolarNoonDate1 = LocalTime.of(12, 56)
        val capeTownExpectedSolarNoonDate4 = LocalTime.of(12, 49)

        val sanFranciscoExpectedSolarNoonDate2 = LocalTime.of(13, 6)
        val sanFranciscoExpectedSolarNoonDate5 = LocalTime.of(13, 14)

        val beijingExpectedSolarNoonDate1 = LocalTime.of(12, 24)
        val beijingExpectedSolarNoonDate3 = LocalTime.of(12, 23)

        assert(checkIfInMaxAllowedMinuteMargin(
            uioExpectedSolarNoonDate1,
            getSunRiseNoonFall(testDate1, 1.0, uio)[1]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
            uioExpectedSolarNoonDate3,
            getSunRiseNoonFall(testDate3, 1.0, uio)[1]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
            uioExpectedSolarNoonDate5,
            getSunRiseNoonFall(testDate5, 2.0, uio)[1]
        ))

        assert(checkIfInMaxAllowedMinuteMargin(
            brasiliaExpectedSolarNoonDate2,
            getSunRiseNoonFall(testDate2, -3.0, brasilia)[1]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
            brasiliaExpectedSolarNoonDate4,
            getSunRiseNoonFall(testDate4, -2.0, brasilia)[1]
        ))

        assert(checkIfInMaxAllowedMinuteMargin(
            capeTownExpectedSolarNoonDate1,
            getSunRiseNoonFall(testDate1, 2.0, capeTown)[1]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
            capeTownExpectedSolarNoonDate4,
            getSunRiseNoonFall(testDate4, 2.0, capeTown)[1]
        ))

        assert(checkIfInMaxAllowedMinuteMargin(
            sanFranciscoExpectedSolarNoonDate2,
            getSunRiseNoonFall(testDate2, -7.0, sanFrancisco)[1]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
            sanFranciscoExpectedSolarNoonDate5,
            getSunRiseNoonFall(testDate5, -7.0, sanFrancisco)[1]
        ))

        assert(checkIfInMaxAllowedMinuteMargin(
            beijingExpectedSolarNoonDate1,
            getSunRiseNoonFall(testDate1, 8.0, beijing)[1]
        ))
        assert(checkIfInMaxAllowedMinuteMargin(
            beijingExpectedSolarNoonDate3,
            getSunRiseNoonFall(testDate3, 8.0, beijing)[1]
        ))

    }
}

fun checkIfInMaxAllowedMinuteMargin(expectedTime: LocalTime, timeToTest: LocalTime): Boolean {

    println(expectedTime)
    println(timeToTest)

    val minutesMargin: Long = 2
    if(expectedTime.isAfter(timeToTest.plusMinutes(minutesMargin))) return false
    if(expectedTime.isBefore(timeToTest.minusMinutes(minutesMargin))) return false
    return true
}