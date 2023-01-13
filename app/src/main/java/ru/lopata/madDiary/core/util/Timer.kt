package ru.lopata.madDiary.core.util

import android.os.Handler
import android.os.Looper

class Timer(listener: OnTimerTickListener) {
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    private var duration = 0L
    private var delay = 100L

    init {
        runnable = Runnable {
            duration += delay
            handler.postDelayed(runnable, delay)
            listener.onTimerTick(format())
        }
    }

    private fun format(): String {
        val millis = duration % 1000
        val seconds = (duration / 1000) % 60
        val minutes = (duration / 60000) % 60
        val hours = (duration / (60000 * 60))
        return if (hours > 0)
            "%02d:%02d:%02d.%03d".format(hours,minutes,seconds,millis)
        else
            "%02d:%02d.%03d".format(minutes,seconds,millis)
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