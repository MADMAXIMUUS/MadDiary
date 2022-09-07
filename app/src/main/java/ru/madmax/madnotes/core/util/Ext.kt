package ru.madmax.madnotes.core.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.os.Debug
import android.util.Log
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import java.lang.Long.parseLong
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit

fun Activity.setNavigationColor(@ColorInt color: Int) {
    window.navigationBarColor = color
}

fun Activity.isDarkTheme(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SimpleDateFormat")
fun Long.toDate(): String {
    val sdf = SimpleDateFormat("dd.MM.yy HH:mm")
    val netDate = Date(this)
    return sdf.format(netDate)
}