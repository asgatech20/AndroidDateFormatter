package com.example.dateformatter

import android.icu.util.LocaleData
import org.joda.time.*
import org.joda.time.chrono.GregorianChronology
import org.joda.time.chrono.ISOChronology
import org.joda.time.chrono.IslamicChronology
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
/**
 * @usage this object has many methods to deal with date formats
*/
object DateFormatterUtil {
    /**
     * @usage this function is used to parse string date to Date Object based on passed date format
     * @param from A String containing the date string
     * @param format A String represents the required format'
     * @return A Date value that represents the output date
     */
    fun convertStringToDate(from: String, format: String): Date? {
        try {
            return SimpleDateFormat(format).parse(from)
        } catch (ex: ParseException) {
            print(ex.printStackTrace())
        }
        return null

    }
    /**
     * @usage this function is used to format date of type Date to date String based on required date format
     * @param from A Date containing the date object
     * @param format A String represents the required format
     * @return A String value that represents the output date
     */
    fun convertDateToString(from: Date, format: String): String {
        return try {
            SimpleDateFormat(format).format(from)
        } catch (e: ParseException) {
            e.localizedMessage
        }
    }

    /**
     * @usage this function is used to convert date of type Date to hijri date String
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
    fun initTodayIso(calendar:Calendar):LocalDate{
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
     * @param from A LocalDateTime containing the datetime object
     * @return A String that represents the date
     */
    fun convert(date: LocalDateTime, format: String): String {
        return date.toString(format)
    }

    /**
     * @usage this function is used to convert LocalDate object to hijri date String
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
    fun initDateTimeIso(localDate: LocalDate):LocalDate{
        val iso: Chronology = ISOChronology.getInstanceUTC()
        return localDate.toDateTimeAtStartOfDay(DateTimeZone.UTC)
            .withChronology(iso).toLocalDate()
    }
    /**
     * @usage this function is used to convert date to hours and minutes based on UTC time zone
     * @param date A String containing the date
     * @return A String that represents the hours and minutes
     */
    fun convertDateToHoursMinUTC(dateString: String,dateFormat: String="yyyy-MM-dd'T'HH:mm:ss"): String {
        val format = SimpleDateFormat("HH:mm")
        format.timeZone = TimeZone.getTimeZone("UTC")
        val sdf = SimpleDateFormat(dateFormat)
        var d: Date? = null
        try {
            d = sdf.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return format.format(d)
    }
    /**
     * @usage this function is used to convert date string to days and months
     * @param date A String containing the date
     * @return A String that represents the days and months
     */
    fun convertDateToDateDaysMonthsUTC(date: String,dateFormat: String="yyyy-MM-dd'T'HH:mm:ss"): String {
        val format = SimpleDateFormat("dd MMMM")
        format.timeZone = TimeZone.getTimeZone("UTC")
        val sdf = SimpleDateFormat(dateFormat)
        var d: Date? = null
        try {
            d = sdf.parse(date)
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
    fun isFutureDate(fromDate: String , toDate: String): Boolean {
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
     * @param format A String that represents required format
     * @return A String that represents the current date
     */
    fun getCurrentData(format:String): String {
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat(format, Locale.ENGLISH)
        return df.format(c)
    }
    /**
     * @usage this function is used to get time from date String
     * @param georgianDate A String containing the date
     * @param dateFormat A String containing the format of input date
     * valid patterns : "yyyy/MM/dd - hh:mm:ss a" or "yyyy-MM-dd'T'HH:mm:ss"
     * @return A String that represents the sec of given date
     */
    fun getHoursMinFromDate(georgianDate: String,dateFormat:String): String{
        try {
            val calendar = Calendar.getInstance()
            val df: java.text.DateFormat =
                SimpleDateFormat(dateFormat, Locale.ENGLISH)
            val dateLong = df.parse(georgianDate).time
            val formatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
            calendar.timeInMillis = dateLong
            return formatter.format(calendar.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

}

