package ru.lopata.madDiary.core.util

import android.os.Handler
import android.os.Looper

class Timer(listener: OnTimerTickListener) {
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    var duration = 0L
    private var delay = 100L

    init {
        runnable = Runnable {
            duration += delay
            handler.postDelayed(runnable, delay)
            listener.onTimerTick(format())
        }
    }

    private fun format(): String {
        val seconds = (duration / 1000) % 60
        val minutes = (duration / 60000) % 60
        val hours = (duration / (60000 * 60))
        return if (hours > 0)
            "%02d:%02d:%02d".format(hours, minutes, seconds)
        else
            "%02d:%02d".format(minutes, seconds)
    }

    fun start() {
        handler.postDelayed(runnable, delay)
    }

    fun pause() {
        handler.removeCallbacks(runnable)
    }

    fun stop() {
        handler.removeCallbacks(runnable)
        duration = 0L
    }

    interface OnTimerTickListener {
        fun onTimerTick(duration: String)
    }

}