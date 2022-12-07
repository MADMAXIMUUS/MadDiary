package ru.lopata.madDiary.featureReminders.util

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomSheetCallback(
    val onStateChange: (bottomSheet: View, newState: Int) -> Unit = { _, _ -> },
    val onSlideSheet: (bottomSheet: View, newOffset: Float, prevOffset: Float) -> Unit = { _, _, _ -> }
) : BottomSheetBehavior.BottomSheetCallback() {

    private var prevOffset = -1f

    override fun onStateChanged(bottomSheet: View, newState: Int) {
        onStateChange(bottomSheet, newState)
    }

    override fun onSlide(bottomSheet: View, slideOffset: Float) {
        onSlideSheet(bottomSheet, slideOffset, prevOffset)
        prevOffset = slideOffset
    }
}