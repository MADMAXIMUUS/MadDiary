package ru.lopata.madDiary.core.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
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
        .setMessage(message)
        .setNegativeButton(resources.getString(R.string.cancel_button_title)) { dialog, _ ->
            dialog.dismiss()
        }
        .setPositiveButton(resources.getString(R.string.settings_button_title)) { dialog, _ ->
            toSettings()
            dialog.dismiss()
        }
        .show()
}

fun View.expand() {
    measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    val targetHeight: Int = measuredHeight

    layoutParams.height = 1
    visibility = View.VISIBLE

    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            layoutParams.height = if (interpolatedTime == 1f)
                LinearLayout.LayoutParams.WRAP_CONTENT
            else
                (targetHeight * interpolatedTime).toInt()
            requestLayout()

        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    a.duration = (targetHeight / context.resources.displayMetrics.density).toInt().toLong()
    startAnimation(a)
}

fun View.collapse() {
    val initialHeight: Int = measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                visibility = View.GONE
            } else {
                layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    a.duration = (initialHeight / context.resources.displayMetrics.density).toInt().toLong()
    startAnimation(a)
}

fun Long.toDateTime(): String {
    val sdf = SimpleDateFormat("dd.MM.yy HH:mm", Locale("ru"))
    sdf.timeZone = TimeZone.getDefault()
    val netDate = Date(this)
    return sdf.format(netDate)
}

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

fun Int.toTimeDuration(): String {
    val sdf = SimpleDateFormat("mm:ss", Locale("ru"))
    val netDate = Date(this.toLong())
    return sdf.format(netDate)
}

fun Long.toDate(): String {
    val sdf = SimpleDateFormat("dd.MM.yy", Locale("ru"))
    sdf.timeZone = TimeZone.getDefault()
    val netDate = Date(this)
    return sdf.format(netDate)
}

fun Long.toFileName(): String {
    val sdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale("ru"))
    sdf.timeZone = TimeZone.getDefault()
    val netDate = Date(this)
    return sdf.format(netDate)
}