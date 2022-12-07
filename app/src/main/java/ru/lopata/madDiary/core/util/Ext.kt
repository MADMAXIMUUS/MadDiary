package ru.lopata.madDiary.core.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.sql.Date
import java.text.SimpleDateFormat
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

fun Activity.hideKeyboard() {
    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE)
            as InputMethodManager
    imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

fun Activity.checkPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun Activity.requestPermissions(vararg permission: String) {
    ActivityCompat.requestPermissions(this, permission, 200)
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

fun Long.toTimeDuration(): String {
    val sdf = SimpleDateFormat("mm:ss", Locale("ru"))
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