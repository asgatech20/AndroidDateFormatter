package com.example.dateformatter

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.joda.time.DateTimeZone
import org.joda.time.tz.UTCProvider

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DateFormatterInstrumentedTest {
    private lateinit var instrumentationContext: Context
    @Before
    fun init() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    }


}