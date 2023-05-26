package com.example.sunandmoon

import com.example.sunandmoon.ui.components.userInputComponents.validateNumber
import org.junit.Test

class TimePickerUnitTest{
    @Test
    fun testValidateNumberInvalidValue(){
        val result = validateNumber("-52", 0, 59)
        val result2 = validateNumber("", 0, 59)

        assert(result == null)
        assert(result2 == 0)

    }

    @Test
    fun testValidateNumberValidValue(){

        val result = validateNumber("23", 0, 59)
        val result2 = validateNumber("59", 0, 59)

        assert(result == 23)
        assert(result2 == 59)
    }
}