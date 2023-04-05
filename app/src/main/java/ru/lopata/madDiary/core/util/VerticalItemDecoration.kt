package ru.lopata.madDiary.core.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalItemDecoration(
    private val spaceTop: Int,
    private val spaceBottom: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == 0) {
            outRect.top = spaceTop
            outRect.bottom = spaceBottom
        } else {
            outRect.bottom = spaceBottom
            outRect.top = 0
        }
    }
}