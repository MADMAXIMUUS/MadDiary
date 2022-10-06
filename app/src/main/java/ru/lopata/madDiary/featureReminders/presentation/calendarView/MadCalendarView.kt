package ru.lopata.madDiary.featureReminders.presentation.calendarView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.StateSet
import android.view.View
import android.widget.ImageButton
import androidx.viewpager2.widget.ViewPager2
import ru.lopata.madDiary.R

class MadCalendarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    init {
        if (attributeSet != null) {
            initAttributes(attributeSet, defStyleAttr, defStyleAttr)
        }
    }

    private val calendarPager: ViewPager2 = ViewPager2(context)
    private val prevButton = ImageButton(context)
    private val nextButton = ImageButton(context)

    private var changeMonthButtonsColor: Int = CHANGE_MONTH_BUTTONS_COLOR
    private var monthYearTitleColor: Int = MONTH_YEAR_TITLE_COLOR

    private fun initAttributes(
        attributeSet: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        val typedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.MadCalendarView,
            defStyleAttr,
            defStyleRes
        )

        changeMonthButtonsColor = typedArray.getColor(
            R.styleable.MadCalendarView_mad_changeMonthButtonsColor,
            CHANGE_MONTH_BUTTONS_COLOR
        )

        monthYearTitleColor = typedArray.getColor(
            R.styleable.MadCalendarView_mad_monthYearTitleColor,
            CHANGE_MONTH_BUTTONS_COLOR
        )

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {


        /*setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )*/
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

    private companion object {
        const val CHANGE_MONTH_BUTTONS_COLOR = Color.BLACK
        const val MONTH_YEAR_TITLE_COLOR = Color.BLACK
    }
}
