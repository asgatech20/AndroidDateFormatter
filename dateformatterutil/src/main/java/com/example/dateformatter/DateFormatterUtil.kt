package com.example.dateformatter

import android.content.Context
import android.provider.Settings.Global.getString
import com.example.dateformatter.DateFormatterUtil.convertFromIslamic
import org.joda.time.*
import org.joda.time.chrono.GregorianChronology
import org.joda.time.chrono.ISOChronology
import org.joda.time.chrono.IslamicChronology
import java.lang.Exception
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
     * @param dateParser A enum class represents the all valid formats
     * @return A Date value that represents the output date
     */
    fun convertStringToDate(from: String, dateParser: StandardDateParser): Date? {
        try {
            return dateParser.parser.parse(from)
        } catch (ex: ParseException){
            ExceptionLogger.Error(ex.localizedMessage,ex)
        }
        return null

    }

    /**
     * @usage this function is used to format date of type Date to date String based on required date format
     * @acceptedFormats all formats in StandardDateParser enum class if you enter invalid value for entered pattern it will throw exception
     * @param from A Date containing the date object
     * @param dateParser A enum class represents the all valid formats
     * @return A String value that represents the output date
     */
    fun convertDateToString(from: Date?, dateParser: StandardDateParser): String {
        return try {
            dateParser.parser.format(from)
        } catch (ex: ParseException) {
            ExceptionLogger.Error(ex.localizedMessage,ex)
            ""
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
        } catch (ex: ParseException) {
            ExceptionLogger.Error(ex.localizedMessage,ex)
            ""
        }
        return null
    }

    /**
     * @usage this fun is used to init Today Iso from calender obj
     * @param calendar Calendar object indicates date
     * @return LocalDate object
     */
    private fun initTodayIso(calendar: Calendar): LocalDate {
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
        return try {
            date.toString(dateParser.name)
        }catch (ex:Exception){
            ExceptionLogger.Error(ex.localizedMessage,ex)
            ""
        }
    }

    /**
     * @usage this function is used to convert hijri local date object to date String
     * @acceptedFormats all formats in StandardDateParser enum class if you enter invalid value for entered pattern it will throw exception
     * @param localDate A LocalDate containing the hijri date object
     * @return A Date that represents the date
     */
    fun convertFromIslamic(localDate: LocalDate): Date? {
       return try {
            val dtIso = initDateTimeIso(localDate)
            dtIso.toDate()
        }catch (ex:IllegalArgumentException){
            ExceptionLogger.Error("invalid format",ex)
            null
        }
    }

    /**
     * @usage this function is used to init local date based on UTC time zone
     * @param localDate A LocalDate object containing the date
     * @return  LocalDate with UTC time Zone
     */
    private fun initDateTimeIso(localDate: LocalDate): LocalDate {
        val iso: Chronology = ISOChronology.getInstanceUTC()
        return localDate.minusDays(1).toDateTimeAtStartOfDay(DateTimeZone.UTC)
            .withChronology(iso).toLocalDate()
    }

    /**
     * @usage this function is used to check to-date is after from-date
     * @acceptedFormats all date or date-time formats in StandardDateParser enum class if you enter invalid value for entered pattern it will
     * throw exception or if you pass time only it will throw IllegalArgumentException
     * @param firstDate A String containing the start date
     * @param secondDate A String containing the end date
     * @return A Boolean that indicates the end date is after start or not
     */
    fun isAfterDate(firstDate: String,firstDateParser: StandardDateParser, secondDate: String,secondDateParser: StandardDateParser): Boolean ?{
        return try {
            convertStringToDate(secondDate,secondDateParser)?.after(convertStringToDate(firstDate,firstDateParser))
        } catch (ex:IllegalArgumentException){
            ExceptionLogger.Error("check your input dates",ex)
            false
        }
    }

    /**
     * @usage this function is used to get current date
     * @acceptedFormats all formats in StandardDateParser enum class
     * @param dateParser A StandardDateParser enum class contain all valid date and time format
     * @return A String that represents the current date
     */
    fun getCurrentDate(dateParser: StandardDateParser): String {
        val c = Calendar.getInstance().time
        return dateParser.parser.format(c)
    }

}

enum class StandardDateParser(var parser: SimpleDateFormat, val displayName: String) {
    YYYY_MM_DD(SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH), "yyyy-MM-dd"), // ex.2001-07-04
    YYYY_MM_DD_HH_MM_SS(SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH), "yyyy-MM-dd HH:mm:ss"),//ex. 2001-07-04 12:08:56
    DD_MM_YYYY_HH_MM_SS(SimpleDateFormat("dd.MM.yyyy HH:mm:ss",Locale.ENGLISH), "dd.MM.yyyy HH:mm:ss"),//ex. 04.07.2021 12:08:56
    DD_MM_YYYY(SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH), "dd-MM-yyyy"),//ex. 20-12-2021
    HH_MM_A(SimpleDateFormat("hh:mm a",Locale.ENGLISH), "hh:mm a"),//a for AM or PM ex. 12:24 AM
    HH_MM(SimpleDateFormat("HH:mm",Locale.ENGLISH), "HH:mm"),//ex. 12:24
    DD_MMMM(SimpleDateFormat("dd MMMM",Locale.ENGLISH), "dd MMMM"),//ex. 04 July
   // Z for RFC 822 time zone example	-0800  example for Z : 2001-07-04T12:08:56 -0700 / example for z : 2001-07-04T12:08:56 GMT or PDT
    YYYY_MM_DDTHH_MM_SS(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.ENGLISH), "yyyy-MM-dd'T'HH:mm:ss"),// ex. 2001-07-04T12:08:56
    YYYY_MM_DDTHH_MM_SS_A(SimpleDateFormat("yyyy/MM/dd - hh:mm:ss a",Locale.ENGLISH), "yyyy/MM/dd - hh:mm:ss a");// ex. 2001/07/04 - 12:08:56 AM

    override fun toString(): String {
        return displayName
    }

}
fun main(){
    convertFromIslamic(LocalDate("21-2-2023"))
}