package com.example.dateformatter

import org.joda.time.*
import org.joda.time.chrono.GregorianChronology
import org.joda.time.chrono.ISOChronology
import org.joda.time.chrono.IslamicChronology
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @usage this object has many methods to deal with date formats
 * @rule  start of calender : hijri-to-gregorian: 1389-10-23=>1970-01-01
 */
object DateFormatterUtil {
    /**
     * @usage this function is used to parse string date to Date Object based on passed date format
     * @acceptedFormats all formats in StandardDateParser enum class if you enter invalid value for entered pattern it will throw exception
     * @param from A String containing the date string
     * @param format A String represents the required format'
     * @return A Date value that represents the output date
     */
    fun convertStringToDate(from: String, dateParser: StandardDateParser): Date? {
        try {
            return dateParser.parser.parse(from)
        } catch (ex: ParseException){
            ex.stackTrace
        }
        return null

    }

    /**
     * @usage this function is used to format date of type Date to date String based on required date format
     * @acceptedFormats all formats in StandardDateParser enum class if you enter invalid value for entered pattern it will throw exception
     * @param from A Date containing the date object
     * @param format A String represents the required format
     * @return A String value that represents the output date
     */
    fun convertDateToString(from: Date, dateParser: StandardDateParser): String {
        return try {
            dateParser.parser.format(from)
        } catch (e: ParseException) {
            e.localizedMessage
        }
    }

    /**
     * @usage this function is used to convert date of type Date to hijri date String
     * @acceptedFormats all formats in StandardDateParser enum class if you enter invalid value for entered pattern it will throw exception
     * @param from A Date containing the date object
     * @return A LocalDate object that represents the hijri date
     */
    fun convertToHijriDate(from: Date): LocalDate? {
        try {
            val calendar = initCalendar(from)
            val hijri: Chronology = IslamicChronology.getInstance()
            val todayIso = initTodayIso(calendar)
            val todayHijri = LocalDate(
                todayIso.toDateTimeAtStartOfDay(),
                hijri
            )
            return todayHijri
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * @usage this fun is used to init Today Iso from calender obj
     * @param calendar Calendar object indicates date
     * @return LocalDate object
     */
    fun initTodayIso(calendar: Calendar): LocalDate {
        val iso: Chronology = GregorianChronology.getInstance()
        return LocalDate(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH] + 1,
            calendar[Calendar.DAY_OF_MONTH]+1, iso
        )
    }

    /**
     * @usage this fun is used to init calender time in millis from date
     * @param from Date object indicates date
     * @return Calender object
     */
    private fun initCalendar(from: Date): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = from.time
        return calendar
    }

    /**
     * @usage this function is used to convert LocalDateTime object to date String
     * @acceptedFormats all formats in StandardDateParser enum class if you enter invalid value for entered pattern it will throw exception
     * @param date A LocalDateTime containing the datetime object
     * @param dateParser A StandardDateParser enum class contain all valid date and time format
     * @return A String that represents the date
     */
    fun convert(date: LocalDateTime, dateParser: StandardDateParser): String {
        return date.toString(dateParser.name)
    }

    /**
     * @usage this function is used to convert LocalDate object to hijri date String
     * @acceptedFormats all formats in StandardDateParser enum class if you enter invalid value for entered pattern it will throw exception
     * @param localDate A LocalDate containing the date object
     * @param isIslamic A Boolean confirm that the date object
     * @return A Date that represents the localDate is islamic or normal date
     */
    fun convertFromIslamic(localDate: LocalDate, isIslamic: Boolean): Date? {
        return if (isIslamic) {
            val dtIso = initDateTimeIso(localDate)
            println("hijri-to-gregorian: $localDate=>$dtIso")
            dtIso.toDate()
        } else {
            localDate.toDate()
        }
    }

    /**
     * @usage this function is used to init local date based on UTC time zone
     * @param localDate A LocalDate object containing the date
     * @return  LocalDate with UTC time Zone
     */
    fun initDateTimeIso(localDate: LocalDate): LocalDate {
        val iso: Chronology = ISOChronology.getInstanceUTC()
        return localDate.plusDays(-1).toDateTimeAtStartOfDay(DateTimeZone.UTC)
            .withChronology(iso).toLocalDate()
    }

    /**
     * @usage this function is used to convert date to hours and minutes based on UTC time zone(current time -2 hours)
     * @acceptedFormats all formats in StandardDateParser enum class if you enter invalid value for entered pattern it will throw exception
     * @param dateString A String containing the date
     * @param dateParser A StandardDateParser enum class contain all valid date and time format
     * @return A String that represents the hours and minutes
     */
    fun convertDateToHoursMinUTC(
        dateString: String,
        dateParser: StandardDateParser
    ): String {
        val format = StandardDateParser.HH_MM.parser
        format.timeZone = TimeZone.getTimeZone("UTC")
        var d: Date? = null
        try {
            d = dateParser.parser.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return format.format(d)
    }

    /**
     * @usage this function is used to convert date string to days and months
     * @param date A String containing the date
     * @param dateParser A StandardDateParser enum class contain all valid date and time format
     * @return A String that represents the days and months
     */
    fun convertDateToDateDaysMonthsUTC(
        date: String,
        dateParser: StandardDateParser
        //dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss"
    ): String {
        val format = StandardDateParser.DD_MMMM.parser
        format.timeZone = TimeZone.getTimeZone("UTC")
        //val sdf = SimpleDateFormat(dateFormat)
        var d: Date? = null
        try {
            d = dateParser.parser.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return format.format(d)
    }

    /**
     * @usage this function is used to check to-date is after from-date
     * @param fromDate A String containing the start date
     * @param toDate A String containing the end date
     * @return A Boolean that indicates the end date is after start or not
     */
    fun isFutureDate(fromDate: String, toDate: String): Boolean {
        return Date(toDate).after(Date(fromDate))
    }

    /**
     * @usage this function is used to split hours and min from time based on am and pm
     * @param time A String containing the date
     * @return A String that contains hh:min am/pm
     */
    fun getHoursMinFromTime(time: String): String {
        if (time.contains(":")) {
            val h1 = time.split(":".toRegex()).toTypedArray()
            val hour = h1[0]
            val minute = h1[1]
            return if (time.contains("PM")) "$hour:$minute PM" else "$hour:$minute AM"
        }
        return "invalid input"
    }

    /**
     * @usage this function is used to get current date
     * @param dateParser A StandardDateParser enum class contain all valid date and time format
     * @return A String that represents the current date
     */
    fun getCurrentData(dateParser: StandardDateParser): String {
        val c = Calendar.getInstance().time
        return dateParser.parser.format(c)
    }

    /**
     * @usage this function is used to get time from date String
     * @param georgianDate A String containing the date
     * @param dateParser A StandardDateParser enum class contain all valid date and time format
     * valid patterns : "yyyy/MM/dd - hh:mm:ss a" or "yyyy-MM-dd'T'HH:mm:ss"
     * @return A String that represents the sec of given date
     */
    fun getHoursMinFromDate(georgianDate: String, dateParser: StandardDateParser): String {

        try {
            val calendar = Calendar.getInstance()
            val dateLong = dateParser.parser.parse(georgianDate).time

            calendar.timeInMillis = dateLong
            return StandardDateParser.HH_MM_A.parser.format(calendar.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""
    }

}

enum class StandardDateParser(var parser: SimpleDateFormat, val displayName: String) {
    YYYY_MM_DD(SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH), "yyyy-MM-dd"), // ex.2001.07.04
    YYYY_MM_DD_HH_MM_SS(SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH), "yyyy-MM-dd HH:mm:ss"),//ex. 2001-07-04 12:08:56
    DD_MM_YYYY_HH_MM_SS(SimpleDateFormat("dd.MM.yyyy HH:mm:ss",Locale.ENGLISH), "dd.MM.yyyy HH:mm:ss"),//ex. 04.07.2021 12:08:56
    DD_MM_YYYY(SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH), "dd-MM-yyyy"),//ex. 20-12-2021
    HH_MM_A(SimpleDateFormat("hh:mm a",Locale.ENGLISH), "hh:mm a"),//a for AM or PM ex. 12:24 AM
    HH_MM(SimpleDateFormat("HH:mm",Locale.ENGLISH), "HH:mm"),//ex. 12:24
    DD_MMMM(SimpleDateFormat("dd MMMM",Locale.ENGLISH), "dd MMMM"),//ex. 04 July
    YYYY_MM_DDTHH_MM_SSZ(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz",Locale.ENGLISH), "yyyy-MM-dd'T'HH:mm:ssz"),//z for	General time zone example Pacific Standard Time; PST; GMT-08:00
   // Z for RFC 822 time zone example	-0800  example for Z : 2001-07-04T12:08:56 -0700 / example for z : 2001-07-04T12:08:56 GMT or PDT
    YYYY_MM_DDTHH_MM_SS(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.ENGLISH), "yyyy-MM-dd'T'HH:mm:ss"),// ex. 2001-07-04T12:08:56
    YYYY_MM_DDTHH_MM_SS_A(SimpleDateFormat("yyyy/MM/dd - hh:mm:ss a",Locale.ENGLISH), "yyyy/MM/dd - hh:mm:ss a");// ex. 2001/07/04 - 12:08:56 AM

    override fun toString(): String {
        return displayName
    }

}