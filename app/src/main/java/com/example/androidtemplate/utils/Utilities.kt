package com.example.androidtemplate.utils

import android.annotation.SuppressLint
import com.example.androidtemplate.data.Log
import java.text.SimpleDateFormat
import java.util.Date

/** Basic formatter to format [Log] timestamps. */
class DateFormatter {

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")

    /** Formats [timestamp] in milliseconds into human readable date. */
    fun formatDate(timestamp: Long): String {
        return formatter.format(Date(timestamp))
    }
}
