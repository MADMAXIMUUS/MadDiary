package ru.lopata.madDiary.featureReminders.presentation.calendarView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.GridLayout

class MadCalendarGrid @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : GridLayout(context, attrs) {

    private val weekDayTitleHeight: Int = 0

    private var cellWidth: Int = 0
    private var cellHeight: Int = 0
    private var cellPadding: Int = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val preferredHeight: Int = cellHeight * 6 + weekDayTitleHeight + paddingTop + paddingBottom
        val preferredWidth: Int = cellWidth * 7 + paddingStart + paddingEnd
        val resolvedWidth = resolveSize(preferredWidth, widthMeasureSpec)
        val resolvedHeight = resolveSize(preferredHeight, heightMeasureSpec)
        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }

    override fun measureChild(
        child: View,
        parentWidthMeasureSpec: Int,
        parentHeightMeasureSpec: Int
    ) {
        val specWidth = MeasureSpec.getSize(parentWidthMeasureSpec)
        val specHeight = MeasureSpec.getSize(parentHeightMeasureSpec)

        /*val childWidthSpec = MeasureSpec.makeMeasureSpec(specWidth / 7, MeasureSpec.AT_MOST)*/

        val childWidthSpec = when (MeasureSpec.getMode(parentWidthMeasureSpec)) {
            MeasureSpec.AT_MOST -> parentWidthMeasureSpec
            MeasureSpec.EXACTLY -> MeasureSpec.makeMeasureSpec(
                (specWidth - 12) / 7, MeasureSpec.AT_MOST
            )
            MeasureSpec.UNSPECIFIED -> parentWidthMeasureSpec
            else -> error("Unreachable")

        }

        /*val childHeightSpec = MeasureSpec.makeMeasureSpec(specHeight / 6, MeasureSpec.AT_MOST)*/

        val childHeightSpec = when (MeasureSpec.getMode(parentHeightMeasureSpec)) {
            MeasureSpec.AT_MOST -> parentHeightMeasureSpec
            MeasureSpec.EXACTLY -> MeasureSpec.makeMeasureSpec(
                (specHeight - 10) / 6, MeasureSpec.AT_MOST
            )
            MeasureSpec.UNSPECIFIED -> parentHeightMeasureSpec
            else -> error("Unreachable")
        }

        child.measure(childWidthSpec, childHeightSpec)
    }

}