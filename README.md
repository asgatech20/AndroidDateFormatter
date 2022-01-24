# AndroidDateFormatter
     this module is used to convert date object to string value with specific formate ,hijri-to-gregorian and vice versa and extract any part of date you need 
## General Rule
     1- start of calender : hijri-to-gregorian: 1389-10-23=>1970-01-01
     2- all formats in StandardDateParser enum class if you enter invalid value for entered pattern it will throw exception
## Usage

### fun convertStringToDate(from: String, dateParser: StandardDateParser): Date?
    this function is used to parse string date to Date Object based on passed date format
 
**Params**
* `from` **{String}** mandatory input
* `dateParser` **{StandardDateParser}** mandatory input
* `returns` **{Date}**

**Example**

```kt
DateFormatterUtil.convertStringToDate(
                "01.02.2022 01:25:27",
                StandardDateParser.DD_MM_YYYY_HH_MM_SS
            )?.year!! + 1900
            //we add +1900 on the result as it is returned -1900
//=>  2022
```

### fun convertDateToString(from: Date?, dateParser: StandardDateParser): String
    this function is used to format date of type Date to date String based on required date format
 
**Params**
* `from` **{Date}** mandatory input
* `dateParser` **{StandardDateParser}** mandatory input
* `returns` **{String}**

**Example**

```kt
val date = DateFormatterUtil.convertStringToDate(
            "01.02.2022 01:32:27",
            StandardDateParser.DD_MM_YYYY_HH_MM_SS
        )!!
            DateFormatterUtil.convertDateToString(
                date, StandardDateParser.DD_MM_YYYY_HH_MM_SS
            )
//=>  01.02.2022 01:32:27
```

### fun convertToHijriDate(from: Date): LocalDate? 
    this function is used to convert date of type Date to hijri date String
 
**Params**
* `from` **{Date}** mandatory input
* `returns` **{LocalDate}**

**Example**

```kt
val date = DateFormatterUtil.convertStringToDate(
                "12.01.2022 01:32:27",
                StandardDateParser.DD_MM_YYYY_HH_MM_SS
            )!!
            DateFormatterUtil.convertToHijriDate(
                date
            )?.dayOfMonth
      
//=>  9 // as it is the equivalent in hijri 
```


### fun convert(date: LocalDateTime, dateParser: StandardDateParser): String 
    this function is used to convert LocalDateTime object to date String
 
**Params**
* `from` **{LocalDateTime}** mandatory input
* `dateParser` **{StandardDateParser}** mandatory input
* `returns` **{String}**

**Example**

```kt
 val localDateTime = LocalDateTime.fromDateFields(
            DateFormatterUtil.convertStringToDate(
                "12 January",
                StandardDateParser.DD_MMMM
            )
        )
      DateFormatterUtil.convert(
                localDateTime ,  StandardDateParser.DD_MM_YYYY
            )
      
      
//=>  12_01_1970 // if you dont pass pattern contain year by default it will be 1970
```


### convertFromIslamic(localDate: LocalDate): Date? 
    this function is used to convert LocalDate object to hijri date String
 
**Params**
* `localDate` **{LocalDate}** mandatory input
* `returns` **{Date?}**

**Example**

```kt
        val date = DateFormatterUtil.convertStringToDate(
            "12-01-2022",
            StandardDateParser.DD_MM_YYYY
        )
        val hijri = DateFormatterUtil.convertToHijriDate(date!!)
        DateFormatterUtil.convertFromIslamic(hijri!!)
      
      
//=>  Wed Jan 12 00:00:00 EET 2022 
```

### fun isFutureDate(fromDate: String, toDate: String): Boolean 
    this function is used to check to-date is after from-date
 
**Params**
* `fromDate` **{String}** mandatory input
* `toDate` **{String}** mandatory input
* `returns` **{Boolean}**

**Example**

```kt
     DateFormatterUtil.isFutureDate("2001/07/04 - 12:08:56 AM", "2022/01/14")
   
//=>  01.02.2022 01:32:27
       
     DateFormatterUtil.isFutureDate("12:08", "01:12")
     
//=> java.lang.IllegalArgumentException as you you must pass datetime or date not time 

```


### fun getHoursMinFromTime(time: String): String
    this function is used to split hours and min from time based on am and pm
 
**Params**
* `time` **{String}** mandatory input
* `returns` **{String}**

**Example**

```kt
    DateFormatterUtil.getHoursMinFromTime("22:17")
   
//=>  22:17 PM
       
   DateFormatterUtil.getHoursMinFromTime("02:17:00 AM")
     
//=> 02:17 AM

```



### fun getCurrentData(dateParser: StandardDateParser): String 
    this function is used to get current date
 
**Params**
* `dateParser` **{StandardDateParser}** mandatory input
* `returns` **{String}**

**Example**

```kt
    DateFormatterUtil.getCurrentData(StandardDateParser.YYYY_MM_DD)
    //=>2022-01-24

```

<details>
<summary><strong>Running Tests</strong></summary>

Running and reviewing unit tests is a great way to get familiarized with this class and its methods. You can find [DateFormatterUtilTest.kt](DateFormatterUtilTest.kt)
 which contains all valid and invalid test cases for every function

</details>
