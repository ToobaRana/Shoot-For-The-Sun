package com.example.sunandmoon

import com.example.sunandmoon.ui.components.userInputComponents.getDayOfFirst
import org.junit.Test

class CalendarUnitTest{
    @Test
    fun testDayOfFirst(){
        val testDay1 = getDayOfFirst(2, 2023)
        val expectedDay1 = "Wednesday"
        assert(testDay1 == expectedDay1)

        val testDay2 = getDayOfFirst(7, 1993)
        val expectedDay2 = "Thursday"
        assert(testDay2 == expectedDay2)


    }
    @Test
    fun testInvalidDayOfFirst(){
        val testDay3 = getDayOfFirst(8, -2)
        val expectedDay3 = "Invalid"
        assert(testDay3 == expectedDay3)
    }
}