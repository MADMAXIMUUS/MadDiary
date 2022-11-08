package ru.lopata.madDiary.core.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import androidx.annotation.ColorInt
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

fun Activity.setNavigationColor(@ColorInt color: Int) {
    window.navigationBarColor = color
}

fun Activity.setStatusBarColor(@ColorInt color: Int) {
    window.statusBarColor = color
}

fun Activity.isDarkTheme(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

@SuppressLint("SimpleDateFormat")
fun Long.toDateTime(): String {
    val sdf = SimpleDateFormat("dd.MM.yy HH:mm", Locale("ru"))
    sdf.timeZone = TimeZone.getDefault()
    val netDate = Date(this)
    return sdf.format(netDate)
}

@SuppressLint("SimpleDateFormat")
fun Long.toTime(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale("ru"))
    sdf.timeZone = TimeZone.getTimeZone("GMT")
    val netDate = Date(this)
    return sdf.format(netDate)
}

fun Long.toTimeZone(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale("ru"))
    val netDate = Date(this)
    return sdf.format(netDate)
}

@SuppressLint("SimpleDateFormat")
fun Long.toDate(): String {
    val sdf = SimpleDateFormat("dd.MM.yy", Locale("ru"))
    sdf.timeZone = TimeZone.getDefault()
    val netDate = Date(this)
    return sdf.format(netDate)
}