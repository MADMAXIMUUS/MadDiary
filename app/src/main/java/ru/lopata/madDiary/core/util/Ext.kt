package ru.lopata.madDiary.core.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import androidx.annotation.ColorInt
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun Activity.setNavigationColor(@ColorInt color: Int) {
    window.navigationBarColor = color
}

fun Activity.isDarkTheme(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

@SuppressLint("SimpleDateFormat")
fun Long.toDateTime(): String {
    val sdf = SimpleDateFormat("dd.MM.yy HH:mm", Locale("ru"))
    val netDate = Date(this)
    Log.e("time", sdf.format(netDate).split(" ")[0])
    return sdf.format(netDate)
}

fun LocalDate.monthYear(): String {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM y")
    return this.format(formatter)
}