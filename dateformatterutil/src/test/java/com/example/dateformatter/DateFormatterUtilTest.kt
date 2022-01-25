package com.example.dateformatter

import android.content.Context
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.tz.UTCProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import java.util.*


class DateFormatterUtilTest {

    lateinit var calender: Calendar


    @Before
    fun init() {
        DateTimeZone.setProvider(UTCProvider())
        calender = Calendar.getInstance()
    }


    @Test
    fun convertStringToDate_return_valid_day_month_year_when_passing_valid_param() {
        //value returned is subtraction of 1900 according to documentation
        assertEquals(
            DateFormatterUtil.convertStringToDate(
                "20 july",
                StandardDateParser.DD_MMMM
            )?.month!! + 1, 7
        )
        assertEquals(
            DateFormatterUtil.convertStringToDate(
                "20:25",
                StandardDateParser.HH_MM
            )?.year!! + 1900, 1970
        )
        //value returned starts from 0 which represents january
        assertEquals(
            DateFormatterUtil.convertStringToDate(
                "2001-07-04",
                StandardDateParser.YYYY_MM_DD
            )?.month!! + 1,//The value returned is between 0 and 11, with the value 0 representing January
            7
        )
        assertEquals(
            DateFormatterUtil.convertStringToDate(
                "2001/07/04 - 12:08:00 AM",
                StandardDateParser.YYYY_MM_DDTHH_MM_SS_A
            )?.month!! + 1,//The value returned is between 0 and 11, with the value 0 representing January
            7
        )
    }

    @Test
    fun convertDateToString_return_valid_String_when_passing_valid_param() {
        calender.set(2001, 7 - 1, 4, 12, 8, 56)
        val date = calender.time

        assertEquals(
            DateFormatterUtil.convertDateToString(
                date, StandardDateParser.YYYY_MM_DDTHH_MM_SS_A
            ), "2001/07/04 - 12:08:56 PM"
        )
    }

    @Test
    fun convertDateToString_return_invalid_String_when_passing_invalid_param() {
        calender.set(2022, 12, 1, 1, 23, 27)
        val date = calender.time
        assertNotEquals(
            DateFormatterUtil.convertDateToString(
                date, StandardDateParser.DD_MM_YYYY_HH_MM_SS
            ), "01.02.2022 01:32:27"
        )
    }

    @Test
    fun convertToHijriDate_return_valid_result_when_passing_valid_params() {
        //we type month-1 cause months is 0 based
        calender.set(2022, 1 - 1, 12, 12, 8, 56)
        val date = calender.time
        assertEquals(
            DateFormatterUtil.convertToHijriDate(
                date
            )?.dayOfMonth, LocalDate("1443-06-09").dayOfMonth
        )
        assertEquals(
            DateFormatterUtil.convertToHijriDate(
                date
            )?.monthOfYear, LocalDate("1443-06-09").monthOfYear
        )
    }

    @Test
    fun convertToHijriDate_return_invalid_result_when_passing_invalid_params() {

        calender.set(2022, 14, 12, 1, 32, 27)
        val date = calender.time
        assertNotEquals(
            DateFormatterUtil.convertToHijriDate(
                date
            )?.dayOfMonth, LocalDate("1443-06-09").dayOfMonth
        )
    }

    @Test
    fun convertLocalDateTimeToStringDate_return_invalid_results_when_passing_invalid_params() {
        calender.set(1970, 1, 12)
        val date = calender.time
        val localDateTime = LocalDateTime.fromDateFields(date)
        assertNotEquals(
            DateFormatterUtil.convert(
                localDateTime, StandardDateParser.DD_MM_YYYY
            ),
            "12_01_1970"
        )
    }

    @Test
    fun convertLocalDateTimeToStringDate_return_valid_results_when_passing_valid_params() {

        val localDateTime2 = LocalDateTime.fromDateFields(
            DateFormatterUtil.convertStringToDate(
                "01:32",
                StandardDateParser.HH_MM
            )
        )
        assertEquals(
            DateFormatterUtil.convert(
                localDateTime2, StandardDateParser.DD_MM_YYYY
            ),
            "01_01_1970"
        )
        val localDateTime3 = LocalDateTime.fromDateFields(
            DateFormatterUtil.convertStringToDate(
                "12.01.2022 01:32:27",
                StandardDateParser.DD_MM_YYYY_HH_MM_SS
            )
        )
        assertEquals(
            DateFormatterUtil.convert(
                localDateTime3, StandardDateParser.DD_MM_YYYY
            ),
            "12_01_2022"
        )
    }

    @Test
    fun convertHigriLocalDateToLocalDate_return_valid_result_when_passing_valid_param() {
        //start of calender : hijri-to-gregorian: 1389-10-23=>1970-01-01
        val date1 = DateFormatterUtil.convertStringToDate(
            "12:24",
            StandardDateParser.HH_MM
        )
        val hijri1 = DateFormatterUtil.convertToHijriDate(date1!!)
        assertEquals(
            LocalDate("1970-01-01").toDate(),
            DateFormatterUtil.convertFromIslamic(hijri1!!)
        )
        val date = DateFormatterUtil.convertStringToDate(
            "12-01-2022",
            StandardDateParser.DD_MM_YYYY
        )
        println(date.toString())
        val hijri = DateFormatterUtil.convertToHijriDate(date!!)
        assertEquals(
            LocalDate("2022-01-12").toDate(),
            DateFormatterUtil.convertFromIslamic(hijri!!)
        )

    }

    @Test
    fun convertHigriLocalDateToLocalDate_return_invalid_result_when_passing_invalid_param() {
        //start of calender : hijri-to-gregorian: 1389-10-23=>1970-01-01
        val date1 = DateFormatterUtil.convertStringToDate(
            "26:24",
            StandardDateParser.HH_MM
        )
        val hijri1 = DateFormatterUtil.convertToHijriDate(date1!!)
        assertNotEquals(
            LocalDate("1970-01-01").toDate(),
            DateFormatterUtil.convertFromIslamic(hijri1!!)
        )

    }

    @Test
    fun compareDates_return_valid_result_when_passing_valid_params() {

        assertEquals(
            true,
            DateFormatterUtil.isAfterDate("2001/07/04 - 12:08:56 AM",StandardDateParser.YYYY_MM_DDTHH_MM_SS_A, "2022-01-14",StandardDateParser.YYYY_MM_DD)
        )
    }

    @Test
    fun compareDates_return_invalid_result_when_passing_invalid_params() {

        //add hijri instead of gregorian
        assertNotEquals(
            true,
            DateFormatterUtil.isAfterDate("2034-07-04",StandardDateParser.YYYY_MM_DD, "2022-01-14",StandardDateParser.YYYY_MM_DD)
        )

        //java.lang.IllegalArgumentException
//        assertEquals(
//            true,
//            DateFormatterUtil.isFutureDate("12:08", "01:12")
//        )
    }


    @Test
    fun getCurrentDate_return_valid_result_when_passing_valid_params() {
        assertEquals("2022-01-25", DateFormatterUtil.getCurrentDate(StandardDateParser.YYYY_MM_DD))
    }

    @Test
    fun getCurrentDate_return_invalid_result_when_passing_invalid_params() {
        assertNotEquals(
            "2022-01-26",
            DateFormatterUtil.getCurrentDate(StandardDateParser.YYYY_MM_DD)
        )
    }
}