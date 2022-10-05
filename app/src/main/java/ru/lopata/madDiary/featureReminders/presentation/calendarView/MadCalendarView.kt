package ru.lopata.madDiary.featureReminders.presentation.calendarView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import ru.madmax.madDiary.R
import java.lang.Integer.max
import kotlin.properties.Delegates

class MadCalendarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    init {
        if (attributeSet != null) {
            initAttributes(attributeSet, defStyleAttr, defStyleAttr)
        } else {
            initDefaultValues()
        }
    }

    var calendarTable: CalendarLayout? = null
        set(value) {
            field?.listeners?.remove(listener)
            field = value
            field?.listeners?.add(listener)
            requestLayout()
            invalidate()
        }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        calendarTable?.listeners?.add(listener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        calendarTable?.listeners?.remove(listener)
    }

    private var changeMonthButtonsColor by Delegates.notNull<Int>()
    private var daysOfWeekTitleColor by Delegates.notNull<Int>()
    private var currentMonthColor by Delegates.notNull<Int>()
    private var todayColor by Delegates.notNull<Int>()
    private var selectedDayColor by Delegates.notNull<Int>()
    private var currentMonthDaysColor by Delegates.notNull<Int>()
    private var otherMonthDaysColor by Delegates.notNull<Int>()

    private val tableRect = RectF(0f, 0f, 0f, 0f)
    private var cellWidth: Float = 0f
    private var cellHeight: Float = 0f
    private var cellPadding: Float = 0f

    lateinit var gridPaint: Paint
    lateinit var notCurrentMonthDatePaint: Paint
    lateinit var currentMonthDate: Paint
    lateinit var selectedDate: Paint
    lateinit var todaySquarePaint: Paint
    lateinit var todayTextPaint: Paint

    private val listener: OnCellChangeListener = {

    }

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
        daysOfWeekTitleColor = typedArray.getColor(
            R.styleable.MadCalendarView_mad_daysOfWeekTitleColor,
            DAYS_OF_WEEK_TITLE_COLOR
        )
        currentMonthColor = typedArray.getColor(
            R.styleable.MadCalendarView_mad_currentMonthColor,
            CURRENT_MONTH_COLOR
        )
        todayColor = typedArray.getColor(
            R.styleable.MadCalendarView_mad_todayColor,
            TODAY_COLOR
        )
        selectedDayColor = typedArray.getColor(
            R.styleable.MadCalendarView_mad_selectedDayColor,
            SELECTED_DAY_COLOR
        )
        currentMonthDaysColor = typedArray.getColor(
            R.styleable.MadCalendarView_mad_currentMonthDaysColor,
            CURRENT_MONTH_DAYS_COLOR
        )
        otherMonthDaysColor = typedArray.getColor(
            R.styleable.MadCalendarView_mad_otherMonthDaysColor,
            OTHER_MONTH_DAYS_COLOR
        )

        typedArray.recycle()
    }

    private fun initDefaultValues() {
        changeMonthButtonsColor = CHANGE_MONTH_BUTTONS_COLOR
        daysOfWeekTitleColor = DAYS_OF_WEEK_TITLE_COLOR
        currentMonthColor = CURRENT_MONTH_COLOR
        todayColor = TODAY_COLOR
        selectedDayColor = SELECTED_DAY_COLOR
        currentMonthDaysColor = CURRENT_MONTH_DAYS_COLOR
        otherMonthDaysColor = OTHER_MONTH_DAYS_COLOR
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val calendar = this.calendarTable ?: return

        val safeWidth = w - paddingStart - paddingEnd
        val safeHeight = h - paddingTop - paddingTop

        cellWidth = safeWidth / calendar.columns.toFloat()
        cellHeight = safeHeight / calendar.rows.toFloat()

        cellPadding = 5f

        val fieldWidth = cellWidth * calendar.columns
        val fieldHeight = cellHeight * calendar.columns

        tableRect.left = paddingLeft + (safeWidth - fieldWidth) / 2
        tableRect.top = paddingTop + (safeHeight - fieldHeight) / 2
        tableRect.right = tableRect.left + fieldWidth
        tableRect.bottom = tableRect.top + fieldHeight
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth = suggestedMinimumWidth + paddingStart + paddingEnd
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val desiredCellWidthInPixel = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            DESIRED_CELL_WIDTH,
            resources.displayMetrics
        ).toInt()

        val desiredCellHeightInPixel = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            DESIRED_CELL_HEIGHT,
            resources.displayMetrics
        ).toInt()

        val rows = calendarTable?.rows ?: 0
        val columns = calendarTable?.columns ?: 0

        val calculatedWidth = columns * desiredCellWidthInPixel + paddingStart + paddingEnd
        val calculatedHeight = rows * desiredCellHeightInPixel + paddingTop + paddingBottom

        val desiredWidth = max(minWidth, calculatedWidth)
        val desiredHeight = max(minHeight, calculatedHeight)

        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

    private companion object {
        const val CHANGE_MONTH_BUTTONS_COLOR = Color.BLACK
        const val DAYS_OF_WEEK_TITLE_COLOR = Color.BLACK
        const val CURRENT_MONTH_COLOR = Color.BLACK
        const val TODAY_COLOR = Color.GREEN
        const val SELECTED_DAY_COLOR = Color.BLUE
        const val CURRENT_MONTH_DAYS_COLOR = Color.BLACK
        const val OTHER_MONTH_DAYS_COLOR = Color.GRAY

        const val DESIRED_CELL_WIDTH = 20f
        const val DESIRED_CELL_HEIGHT = 40f

    }
}