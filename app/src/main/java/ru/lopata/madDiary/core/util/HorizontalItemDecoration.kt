package ru.lopata.madDiary.core.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalItemDecoration(
    private val spaceLeft: Int,
    private val spaceRight: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == -1) return
        when (position) {
            parent.adapter?.itemCount!! - 1 -> {
                outRect.right = 0
                outRect.left = spaceLeft
            }
            0 -> {
                outRect.left = 0
                outRect.right = spaceRight
            }
            else -> {
                outRect.left = spaceLeft
                outRect.right = spaceRight
            }
        }
    }
}