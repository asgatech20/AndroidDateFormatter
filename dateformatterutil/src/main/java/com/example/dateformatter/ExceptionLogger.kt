package com.example.dateformatter

import android.nfc.Tag
import android.util.Log

sealed class ExceptionLogger {
    val TAG: String = ExceptionLogger::class.java.simpleName
     class Error(val message: String?, val cause: Exception? = null) : ExceptionLogger(){
         init {
             printError()
         }
         private fun printError(){
             println("error:${message}\n cause:${cause}")
         }
     }
}