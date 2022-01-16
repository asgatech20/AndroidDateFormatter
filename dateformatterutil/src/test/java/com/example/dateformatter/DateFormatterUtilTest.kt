package com.example.dateformatter

import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.tz.UTCProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class DateFormatterUtilTest {

    @Before
    fun init() {
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
            DateFormatterUtil.convertToHijriDate(
                DateFormatterUtil.convertStringToDate(
                    "12.01.2022 01:32:27",
                    "dd.MM.yyyy HH:mm:ss"
                )!!
            )?.dayOfMonth, LocalDate("1443-06-09").dayOfMonth
        )
    }

    @Test
    fun convertLocalDateTimeToStringDate() {
        assertEquals(
            DateFormatterUtil.convert(
                LocalDateTime.fromDateFields(
                    DateFormatterUtil.convertStringToDate(
                        "12.01.2022 01:32:27",
                        "dd.MM.yyyy HH:mm:ss"
                    )
                ), "dd.MM.yyyy"
            ),
            "12.01.2022"
        )
    }

    @Test
    fun convertHigriLocalDateToLocalDate() {
        val date = com.example.dateformatter.DateFormatterUtil.convertStringToDate(
            "12-01-2022",
            "dd-MM-yyyy"
        )
        println(date.toString())
        val hijri = com.example.dateformatter.DateFormatterUtil.convertToHijriDate(date!!)
        assertEquals(
            LocalDate("2022-01-12").toDate(),
            com.example.dateformatter.DateFormatterUtil.convertFromIslamic(hijri!!, true)
        )
    }

    @Test
    fun convertDateToHoursMinUTC() {
        assertEquals(
            "10:17",
            com.example.dateformatter.DateFormatterUtil.convertDateToHoursMinUTC("2022-01-13T12:17:00")
        )
    }

    @Test
    fun convertDateToDateDaysMonthsUTC() {
        assertEquals(
            "13 January",
            com.example.dateformatter.DateFormatterUtil.convertDateToDateDaysMonthsUTC("2022-01-13T12:17:00")
        )
    }

    @Test
    fun compareDates() {
        assertEquals(
            true,
            com.example.dateformatter.DateFormatterUtil.isFutureDate("2022/01/13", "2022/01/14")
        )
    }

    @Test
    fun getDatFromPmAmHoursMin() {
        assertEquals(
            "22:17 PM",
            DateFormatterUtil.getHoursMinFromTime("22:17:00 PM")
        )
        assertEquals(
            "02:17 AM",
            DateFormatterUtil.getHoursMinFromTime("02:17:00 AM")
        )
    }

    @Test
    fun getCurrentDate() {
        assertEquals("16.01.2022", DateFormatterUtil.getCurrentData("dd.MM.yyyy"))
    }

    @Test
    fun convertTimeFromMillSecondToSecond() {
        assertEquals(
            "09:45 AM",
            DateFormatterUtil.getHoursMinFromDate("2012-10-01T09:45:00", "yyyy-MM-dd'T'HH:mm:ss")
        )
        assertEquals(
            "09:45 AM",
            DateFormatterUtil.getHoursMinFromDate(
                "2012/10/01 - 09:45:00 AM",
                "yyyy/MM/dd - hh:mm:ss a"
            )
        )
    }

}