package com.example.dateformater

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
            DateFormatterUtil.convertStringToDate(
                "01.02.2022 01:32:27",
                "dd.MM.yyyy HH:mm:ss"
            )?.year!! + 1900, 2022
        )
        //value returned passed on enum in documentation
        assertEquals(
            DateFormatterUtil.convertStringToDate(
                "12.01.2022 01:32:27",
                "dd.MM.yyyy HH:mm:ss"
            )?.day!!, 3
        )
        //value returned starts from 0 which represents january
        assertEquals(
            DateFormatterUtil.convertStringToDate(
                "12.01.2022 01:32:27",
                "dd.MM.yyyy HH:mm:ss"
            )?.month!!, 0
        )
    }

    @Test
    fun convertDateToString_return_valid_String_when_passing_valid_param() {
        assertEquals(
            DateFormatterUtil.convertDateToString(
                DateFormatterUtil.convertStringToDate(
                    "01.02.2022 01:32:27",
                    "dd.MM.yyyy HH:mm:ss"
                )!!, "dd.MM.yyyy HH:mm:ss"
            ), "01.02.2022 01:32:27"
        )
    }

    @Test
    fun convertToHijriDate() {
        assertEquals(
            DateFormatterUtil.convertToHijriDate(
                DateFormatterUtil.convertStringToDate(
                    "12.01.2022 01:32:27",
                    "dd.MM.yyyy HH:mm:ss"
                )!!
            )?.year, LocalDate("1443-06-08").year
        )
    }

    @Test
    fun convertLocalDateTimeToStringDate(){
        assertEquals(
           DateFormatterUtil.convert(LocalDateTime.fromDateFields(DateFormatterUtil.convertStringToDate(
               "12.01.2022 01:32:27",
               "dd.MM.yyyy HH:mm:ss"
           )),"dd.MM.yyyy"),
            "12.01.2022"
        )
    }

    @Test
    fun convertHigriLocalDateToLocalDate(){
        val date = DateFormatterUtil.convertStringToDate(
            "12-01-2022",
            "dd-MM-yyyy"
        )
        println(date.toString())
        val hijri = DateFormatterUtil.convertToHijriDate(date!!)
        assertEquals(LocalDate("2022-01-12").toDate(),DateFormatterUtil.convertFromIslamic(hijri!!,true))
    }
    @Test
    fun convertDateToHoursMinUTC(){
        assertEquals("10:17",DateFormatterUtil.convertDateToHoursMinUTC("2022-01-13T12:17:00"))
    }
    @Test
    fun convertDateToDateDaysMonthsUTC(){
        assertEquals("13 January",DateFormatterUtil.convertDateToDateDaysMonthsUTC("2022-01-13T12:17:00"))
    }
    @Test
    fun compareDates(){
        assertEquals(true,DateFormatterUtil.compareDates("2022/01/13","2022/01/14"))
    }
    @Test
    fun getDatFromPmAmHoursMin(){
        assertEquals("22:17 PM",DateFormatterUtil. getDatFromPmAmHoursMin("22:17:00 PM"))
        assertEquals("02:17 AM",DateFormatterUtil. getDatFromPmAmHoursMin("02:17:00 AM"))
    }
    @Test
    fun getCurrentDate(){
        assertEquals("2022/01/13",DateFormatterUtil. getCurrentData())
    }

}