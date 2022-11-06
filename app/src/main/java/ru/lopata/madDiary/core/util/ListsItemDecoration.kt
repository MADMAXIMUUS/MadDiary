package ru.lopata.madDiary.core.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.lopata.madDiary.featureReminders.domain.model.MainScreenItem

class ListsItemDecoration(
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
        if (position == -1) return
        val viewType = parent.adapter?.getItemViewType(position)
        if (viewType == MainScreenItem.TITLE) {
            if (position == 0) {
                outRect.top = spaceTop
                outRect.bottom = spaceBottom
            } else {
                outRect.bottom = spaceBottom
                outRect.top = spaceTop * 4
            }
        } else {
            if (position == parent.adapter?.itemCount!! - 1) {
                outRect.top = spaceTop
                outRect.bottom = 330
            } else {
                outRect.bottom = spaceBottom
                outRect.top = spaceTop
            }
        }
    }
}