package ru.lopata.madDiary.core.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CalendarGridDecoration(
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spanCount = 7
        val spacing = 4
        val position = parent.getChildAdapterPosition(view)

        if (position < spanCount) {
            outRect.top = spacing;
        }
        outRect.bottom = spacing
    }
}