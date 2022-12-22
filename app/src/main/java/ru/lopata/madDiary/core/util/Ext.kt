package ru.lopata.madDiary.core.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.lopata.madDiary.R
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

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.showPermissionDialog(message: String, toSettings: () -> Unit) {
    MaterialAlertDialogBuilder(this)
        .setTitle(resources.getString(R.string.permission_dialog_title))
        .setMessage(resources.getString(R.string.permission_dialog_text))
        .setNegativeButton(resources.getString(R.string.cancel_button_title)) { dialog, _ ->
            dialog.dismiss()
        }
        .setPositiveButton(resources.getString(R.string.settings_button_title)) { dialog, _ ->
            toSettings()
            dialog.dismiss()
        }
        .show()
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