package com.example.dateformater

import android.text.format.DateFormat
import android.util.Log
import org.joda.time.*
import org.joda.time.chrono.GregorianChronology
import org.joda.time.chrono.ISOChronology
import org.joda.time.chrono.IslamicChronology
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object DateFormatterUtil {
    //enum dateformat
    /**
     * @usage this function is used to convert date of type string to date based on required date pattern
     * @param from A String containing the date string
     * @param outputDateFormatPattern A String represents required date format
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
     * @usage this function is used to convert date of type Date to date String based on required date pattern
     * @param from A Date containing the date object
     * @param format A String represents input date format
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
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = from.time
            val iso: Chronology = GregorianChronology.getInstance()
            val hijri: Chronology = IslamicChronology.getInstance()

            val todayIso = LocalDate(
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH] + 1,
                calendar[Calendar.DAY_OF_MONTH], iso
            )
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
     * @usage this function is used to convert LocalDateTime object to hijri date String
     * @param from A LocalDateTime containing the datetime object
     * @return A String that represents the date
     */
    fun convert(date: LocalDateTime, format: String): String {
        return date.toString(format)
    }

    /**
     * @usage this function is used to convert LocalDateTime object to hijri date String
     * @param localDate A LocalDate containing the date object
     * @param isIslamic A Boolean confirm that the date object
     * @return A Date that represents the localDate is islamic or normal date
     */
    fun convertFromIslamic(localDate: LocalDate, isIslamic: Boolean): Date? {
        return if (isIslamic) {
            val iso: Chronology = ISOChronology.getInstanceUTC()
            val dtIso = localDate.toDateTimeAtStartOfDay(DateTimeZone.UTC)
                .withChronology(iso).toLocalDate()
            println("hijri-to-gregorian: $localDate=>$dtIso")
            dtIso.toDate()
        } else {
            localDate.toDate()
        }
    }
    /**
     * @usage this function is used to convert date to hours and minutes based on UTC time zone
     * @param date A String containing the date
     * @return A String that represents the hours and minutes
     */
    fun convertDateToHoursMinUTC(dateString: String): String {
        val format = SimpleDateFormat("HH:mm")
        format.timeZone = TimeZone.getTimeZone("UTC")
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
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
    fun convertDateToDateDaysMonthsUTC(date: String): String {
        val format = SimpleDateFormat("dd MMMM")
        format.timeZone = TimeZone.getTimeZone("UTC")
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        var d: Date? = null
        try {
            d = sdf.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return format.format(d)
    }
    /**
     * @usage this function is used to compare two date
     * @param fromDate A String containing the start date
     * @param toDate A String containing the end date
     * @return A Boolean that indicates the end date is after start or not
     */
    fun compareDates(fromDate: String , toDate: String): Boolean {
        return Date(toDate).after(Date(fromDate))
    }
    /**
     * @usage this function is used to split hours and min from date based on am and pm
     * @param date A String containing the date
     * @return A String that contains hh:min am/pm
     */
    fun getDatFromPmAmHoursMin(date: String): String {
        val h1 = date.split(":".toRegex()).toTypedArray()
        val hour = h1[0]
        val minute = h1[1]
        return if (date.contains("PM")) "$hour:$minute PM" else "$hour:$minute AM"
    }
    /**
     * @usage this function is used to get current date
     * @return A String that represents the current date
     */
    fun getCurrentData(): String {
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
        return df.format(c)
    }
    /**
     * @usage this function is used to convert time from mill sec to sec
     * @param georgianDate A String containing the date
     * @return A String that represents the sec of given date
     */
    fun convertTimeFromMillSecondToSecond(georgianDate: String): String{
        try {
            val calendar = Calendar.getInstance()
            val df: java.text.DateFormat =
                SimpleDateFormat("yyyy/MM/dd - hh:mm:ss a", Locale.ENGLISH)
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

