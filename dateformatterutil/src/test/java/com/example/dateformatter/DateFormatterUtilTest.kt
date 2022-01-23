package com.example.dateformatter

import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.tz.UTCProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.logging.SimpleFormatter
import kotlin.time.ExperimentalTime
import kotlin.time.minutes


class DateFormatterUtilTest {

    @Before
    fun init() {
        //this line solve the problem : ZoneInfoMap not found
        DateTimeZone.setProvider(UTCProvider())
    }

    @ExperimentalTime
    @Test
    fun convertStringToDate_return_valid_day_month_year_when_passing_valid_param() {
        //value returned is subtraction of 1900 according to documentation
        assertEquals(
            DateFormatterUtil.convertStringToDate(
                "01.02.2022 01:25:27",
                StandardDateParser.DD_MM_YYYY_HH_MM_SS
            )?.year!! + 1900, 2022
        )
        assertEquals(
            DateFormatterUtil.convertStringToDate(
                "20 july",
                StandardDateParser.DD_MMMM
            )?.month!!+1, 7
        )
        assertEquals(
            DateFormatterUtil.convertStringToDate(
                "20:25",
                StandardDateParser.HH_MM
            )?.year!! + 1900, 1970
        )
        //value returned passed on enum in documentation
        assertEquals(
            DateFormatterUtil.convertStringToDate(
                "2001-07-04T10:08:56 GMT",
                StandardDateParser.YYYY_MM_DDTHH_MM_SSZ
            )?.day!!// The returned value (0 = Sunday, 1 = Monday, 2 = Tuesday, 3 = Wednesday, 4 = Thursday, 5 = Friday, 6 = Saturday)
            , 3
        )
        //value returned starts from 0 which represents january
        assertEquals(
            DateFormatterUtil.convertStringToDate(
                "2001-07-04",
                StandardDateParser.YYYY_MM_DD
            )?.month!!+1,//The value returned is between 0 and 11, with the value 0 representing January
            7
        )
        assertEquals(
            DateFormatterUtil.convertStringToDate(
                "2001/07/04 - 12:08:00 AM",
                StandardDateParser.YYYY_MM_DDTHH_MM_SS_A
            )?.month!!+1,//The value returned is between 0 and 11, with the value 0 representing January
            7
        )
    }

    @Test
    fun convertDateToString_return_valid_String_when_passing_valid_param() {
        val date1 = DateFormatterUtil.convertStringToDate(
            "01.02.2022 01:32:27",
            StandardDateParser.DD_MM_YYYY_HH_MM_SS
        )!!
        assertEquals(
            DateFormatterUtil.convertDateToString(
                date1, StandardDateParser.DD_MM_YYYY_HH_MM_SS
            ), "01.02.2022 01:32:27"
        )
        val date2  = DateFormatterUtil.convertStringToDate(
            "2001/07/04 - 12:08:56 AM",
            StandardDateParser.YYYY_MM_DDTHH_MM_SS_A
        )!!
        assertEquals(
            DateFormatterUtil.convertDateToString(
                date2, StandardDateParser.YYYY_MM_DDTHH_MM_SS_A
            ), "2001/07/04 - 12:08:56 AM"
        )
    }

    @Test
    fun convertToHijriDate() {
        val date1 = DateFormatterUtil.convertStringToDate(
                "12.01.2022 01:32:27",
                StandardDateParser.DD_MM_YYYY_HH_MM_SS
            )!!
        assertEquals(
            DateFormatterUtil.convertToHijriDate(
                date1
            )?.dayOfMonth, LocalDate("1443-06-09").dayOfMonth
        )
        val date2 =  DateFormatterUtil.convertStringToDate(
            "2022/01/12 - 12:08:56 AM",
            StandardDateParser.YYYY_MM_DDTHH_MM_SS_A
        )!!
        assertEquals(
            DateFormatterUtil.convertToHijriDate(
                date2
            )?.dayOfMonth, LocalDate("1443-06-09").dayOfMonth
        )
        val date3 =  DateFormatterUtil.convertStringToDate(
            "20 January",//year by default 1970 == to 1389 in hijri
            StandardDateParser.DD_MMMM
        )!!
        assertEquals(
            DateFormatterUtil.convertToHijriDate(
                date3
            )?.monthOfYear, LocalDate("1389-10-20").monthOfYear+1
        )
        val date4 =  DateFormatterUtil.convertStringToDate(
            "12:45",//year by default 1970 == to 1389 in hijri
            StandardDateParser.HH_MM
        )!!
        assertEquals(
            DateFormatterUtil.convertToHijriDate(
                date4
            )?.monthOfYear, LocalDate("1389-10-20").monthOfYear
        )
    }

    @Test
    fun convertLocalDateTimeToStringDate() {
        val localDateTime1 = LocalDateTime.fromDateFields(
            DateFormatterUtil.convertStringToDate(
                "12 January",
                StandardDateParser.DD_MMMM
            )
        )
        assertEquals(
            DateFormatterUtil.convert(
                localDateTime1 ,  StandardDateParser.DD_MM_YYYY
            ),
            "12_01_1970"
        )
        val localDateTime2 = LocalDateTime.fromDateFields(
            DateFormatterUtil.convertStringToDate(
                "01:32",
                StandardDateParser.HH_MM
            )
        )
        assertEquals(
            DateFormatterUtil.convert(
                localDateTime2 ,  StandardDateParser.DD_MM_YYYY
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
                localDateTime3,  StandardDateParser.DD_MM_YYYY
            ),
            "12_01_2022"
        )
    }

    @Test
    fun convertHigriLocalDateToLocalDate() {
        //start of calender : hijri-to-gregorian: 1389-10-23=>1970-01-01
        val date1 = DateFormatterUtil.convertStringToDate(
            "12:24",
            StandardDateParser.HH_MM
        )
        val hijri1 = DateFormatterUtil.convertToHijriDate(date1!!)
        assertEquals(
            LocalDate("1970-01-01").toDate(),
            DateFormatterUtil.convertFromIslamic(hijri1!!, true)
        )
        val date = DateFormatterUtil.convertStringToDate(
            "12-01-2022",
            StandardDateParser.DD_MM_YYYY
        )
        println(date.toString())
        val hijri = DateFormatterUtil.convertToHijriDate(date!!)
        assertEquals(
            LocalDate("2022-01-12").toDate(),
            DateFormatterUtil.convertFromIslamic(hijri!!, true)
        )
    }

    @Test
    fun convertDateToHoursMinUTC() {
        assertEquals(
            "10:17",
            DateFormatterUtil.convertDateToHoursMinUTC("2022-01-13T12:17:00",StandardDateParser.YYYY_MM_DDTHH_MM_SS)
        )
        assertEquals(
            "12:08",
            DateFormatterUtil.convertDateToHoursMinUTC("2001-07-04T12:08:56 GMT",StandardDateParser.YYYY_MM_DDTHH_MM_SSZ)
        )
        assertEquals(
            "21:00",
            DateFormatterUtil.convertDateToHoursMinUTC("08 July",StandardDateParser.DD_MMMM)
        )
        assertEquals(
            "21:08",
            DateFormatterUtil.convertDateToHoursMinUTC("2001/07/04 - 12:08:56 AM",StandardDateParser.YYYY_MM_DDTHH_MM_SS_A)
        )
    }

    @Test
    fun convertDateToDateDaysMonthsUTC() {
        assertEquals(
            "13 January",
            DateFormatterUtil.convertDateToDateDaysMonthsUTC("2022-01-13T12:17:00",StandardDateParser.YYYY_MM_DDTHH_MM_SS)
        )
    }

    @Test
    fun compareDates() {
        assertEquals(
            true,
            DateFormatterUtil.isFutureDate("2022/01/13", "2022/01/14")
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
        assertEquals("23-01-2022", DateFormatterUtil.getCurrentData(StandardDateParser.DD_MM_YYYY))
    }

    @Test
    fun convertTimeFromMillSecondToSecond() {
        assertEquals(
            "09:45 AM",
            DateFormatterUtil.getHoursMinFromDate("2012-10-01T09:45:00", StandardDateParser.YYYY_MM_DDTHH_MM_SS)
        )
        assertEquals(
            "09:45 AM",
            DateFormatterUtil.getHoursMinFromDate(
                "2012-10-01  09:45:00 AM",
                StandardDateParser.YYYY_MM_DD_HH_MM_SS
            )
        )
        assertEquals(
            "09:45 AM",
            DateFormatterUtil.getHoursMinFromDate(
                "2012-10-01T09:45:00",
                StandardDateParser.YYYY_MM_DDTHH_MM_SS
            )
        )

    }

}