# AndroidDateFormatter
     this module is used to convert date object to string value with specific formate and vice versa ,hijri-to-gregorian and vice versa and extract any part of date you need 
## General Rule
     1- start of calender : hijri-to-gregorian: 1389-10-23=>1970-01-01
     2- all formats in StandardDateParser enum class if you enter invalid value for entered pattern it will throw exception
     3- **{StandardDateParser }**
           is enum class that contain all formates that you can use to parse and formate dates
## Usage

### fun convertStringToDate(from: String, dateParser: StandardDateParser): Date?
    this function is used to parse string date to Date Object based on passed date format
 
**Params**
* `from` **{String}** mandatory input
* `dateParser` **{StandardDateParser}** mandatory input
* `returns` **{Date}**

**Example**
this example illustrates
```kt
//here we convert string date to date object based on its format
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
//here we get date object from calender then we convert it to string based on passed standard date parsser object 
 val calender = Calendar.getInstance()
 calender.set(2001, 7 - 1, 4, 12, 8, 56)
        val date = calender.time
        DateFormatterUtil.convertDateToString(
                date, StandardDateParser.YYYY_MM_DDTHH_MM_SS_A
            )
        )
//=>  2001/07/04 - 12:08:56 PM
```

### fun convertToHijriDate(from: Date): LocalDate? 
    this function is used to convert date of type Date to hijri date String
 
**Params**
* `from` **{Date}** mandatory input
* `returns` **{LocalDate}**

**Example**

```kt
//here we convert string to date object then convert it to hijri date and extact day of month from it
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
// here we build local date time object from date then we convert that object to date string based on desired parser you choose
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
// here we create date  object from date string then we convert that object to hijri object then we convert it back to normal date 

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
// here we compare between two dates if second date after first date or not

     DateFormatterUtil.isFutureDate("2001/07/04 - 12:08:56 AM", "2022/01/14")
   
//=>  true
       
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
//here we split hours and min from time with adding am or pm
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
    //here we get current date based on desired format you choose
    DateFormatterUtil.getCurrentData(StandardDateParser.YYYY_MM_DD)
    //=>2022-01-24

```

<details>
<summary><strong>Running Tests</strong></summary>

Running and reviewing unit tests is a great way to get familiarized with this class and its methods. You can find [DateFormatterUtilTest.kt](https://github.com/asgatech20/AndroidDateFormatter/blob/development/dateformatterutil/src/test/java/com/example/dateformatter/DateFormatterUtilTest.kt)
 which contains all valid and invalid test cases for every function

</details>
