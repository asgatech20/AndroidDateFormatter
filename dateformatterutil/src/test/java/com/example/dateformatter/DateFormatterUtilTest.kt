package com.example.dateformatter

import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.tz.UTCProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*


class DateFormatterUtilTest{

    @Before
    fun init(){
        //this line solve the problem : ZoneInfoMap not found
        DateTimeZone.setProvider(UTCProvider())
    }
    @Test
    fun convertStringToDate_return_valid_day_month_year_when_passing_valid_param() {
        //value returned is subtraction of 1900 according to documentation
        assertEquals(
            com.example.dateformatter.DateFormatterUtil.convertStringToDate(
                "01.02.2022 01:32:27",
                "dd.MM.yyyy HH:mm:ss"
            )?.year!! + 1900, 2022
        )
        //value returned passed on enum in documentation
        assertEquals(
            com.example.dateformatter.DateFormatterUtil.convertStringToDate(
                "12.01.2022 01:32:27",
                "dd.MM.yyyy HH:mm:ss"
            )?.day!!, 3
        )
        //value returned starts from 0 which represents january
        assertEquals(
            com.example.dateformatter.DateFormatterUtil.convertStringToDate(
                "12.01.2022 01:32:27",
                "dd.MM.yyyy HH:mm:ss"
            )?.month!!, 0
        )
    }

    @Test
    fun convertDateToString_return_valid_String_when_passing_valid_param() {
        assertEquals(
            com.example.dateformatter.DateFormatterUtil.convertDateToString(
                com.example.dateformatter.DateFormatterUtil.convertStringToDate(
                    "01.02.2022 01:32:27",
                    "dd.MM.yyyy HH:mm:ss"
                )!!, "dd.MM.yyyy HH:mm:ss"
            ), "01.02.2022 01:32:27"
        )
    }

    @Test
    fun convertToHijriDate() {
        assertEquals(
            com.example.dateformatter.DateFormatterUtil.convertToHijriDate(
                com.example.dateformatter.DateFormatterUtil.convertStringToDate(
                    "12.01.2022 01:32:27",
                    "dd.MM.yyyy HH:mm:ss"
                )!!
            )?.year, LocalDate("1443-06-08").year
        )
    }

    @Test
    fun convertLocalDateTimeToStringDate(){
        assertEquals(
           com.example.dateformatter.DateFormatterUtil.convert(LocalDateTime.fromDateFields(
               com.example.dateformatter.DateFormatterUtil.convertStringToDate(
               "12.01.2022 01:32:27",
               "dd.MM.yyyy HH:mm:ss"
           )),"dd.MM.yyyy"),
            "12.01.2022"
        )
    }

    @Test
    fun convertHigriLocalDateToLocalDate(){
        val date = com.example.dateformatter.DateFormatterUtil.convertStringToDate(
            "12-01-2022",
            "dd-MM-yyyy"
        )
        println(date.toString())
        val hijri = com.example.dateformatter.DateFormatterUtil.convertToHijriDate(date!!)
        assertEquals(LocalDate("2022-01-12").toDate(),
            com.example.dateformatter.DateFormatterUtil.convertFromIslamic(hijri!!,true))
    }
    @Test
    fun convertDateToHoursMinUTC(){
        assertEquals("10:17", com.example.dateformatter.DateFormatterUtil.convertDateToHoursMinUTC("2022-01-13T12:17:00"))
    }
    @Test
    fun convertDateToDateDaysMonthsUTC(){
        assertEquals("13 January", com.example.dateformatter.DateFormatterUtil.convertDateToDateDaysMonthsUTC("2022-01-13T12:17:00"))
    }
    @Test
    fun compareDates(){
        assertEquals(true, com.example.dateformatter.DateFormatterUtil.compareDates("2022/01/13","2022/01/14"))
    }
    @Test
    fun getDatFromPmAmHoursMin(){
        assertEquals("22:17 PM", com.example.dateformatter.DateFormatterUtil. getDatFromPmAmHoursMin("22:17:00 PM"))
        assertEquals("02:17 AM", com.example.dateformatter.DateFormatterUtil. getDatFromPmAmHoursMin("02:17:00 AM"))
    }
    @Test
    fun getCurrentDate(){
        assertEquals("2022/01/13", com.example.dateformatter.DateFormatterUtil. getCurrentData())
    }

}