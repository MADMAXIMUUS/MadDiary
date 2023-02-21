package ru.lopata.madDiary.featureReminders.presentation.calendarView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.icu.util.ULocale
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import ru.lopata.madDiary.R
import ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states.CalendarDayState
import ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states.EventInCalendarGrid

class MadCalendarMonth @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var calendarDayStateList: Map<Calendar, List<EventInCalendarGrid>> = emptyMap()

    private var onDayClickListener: OnDayClickListener? = null

    private var monthNumber = 0
    private var yearNumber = 1970

    private val daysOfWeekPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val currentMonthDaysPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val todayTitlePaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val otherMonthDaysPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val eventTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val selectedDayPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val eventBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val currentDayHighlightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val dayOfWeekLabels: Array<String> = resources.getStringArray(R.array.days_in_week)

    private val timeZone = TimeZone.getDefault()
    private val locale = ULocale.forLocale(resources.configuration.locales.get(0))
    private val calendar = Calendar.getInstance(timeZone, locale).apply {
        firstDayOfWeek = Calendar.MONDAY
    }

    private var dayOfWeekHeight = 50
    private var cellHeight = 0
    private var cellWidth = 0
    private var todaySquareWidth = 0
    private var todaySquareTopLeftPoint = PointF(0f, 0f)
    private var selectedDayBorderPadding = 5
    private var eventsRect = RectF(0f, 0f, 0f, 0f)

    private var desiredDayOfWeekHeight = 0
    private var desiredCellWidth = 0
    private var desiredCellHeight = 0
    private var desiredTodaySquareWidth = 0
    private var desiredDayTitleTextSize = 0f
    private var desiredEventTitleTextSize = 0f
    private var desiredDayOfWeekTextSize = 0f

    private var selectedDay = DEFAULT_SELECTED_DAY
    private var today = DEFAULT_SELECTED_DAY
    private var weekStart = DEFAULT_WEEK_START
    private var dayOfWeekStart = 0
    private var daysInMonth = 31
    private var daysInPrevMonth = 31
    private var paddedWidth = 0
    private var paddedHeight = 0

    private var daysOfWeekTitleColor = 0
    private var daysOfWeekTitleTextSize = 0f
    private var gridColor = 0
    private var gridWidth = 1f
    private var dayTitleTextSize = 0f
    private var selectedDayColor = 0
    private var selectedDayWidth = 5f
    private var currentDayColor = 0
    private var currentDayTitleColor = 0
    private var eventTextSize = 0f
    private var eventTitleLightColor = 0
    private var eventTitleDarkColor = 0
    private var currentMonthDaysColor = 0
    private var otherMonthDaysColor = 0

    init {
        if (attrs != null) {
            initAttributes(attrs, defStyleAttr, defStyleAttr)
            initPaints()
        }
        isFocusable = true
        isClickable = true
        if (isInEditMode) {
            setMonthParams(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                -1,
                Calendar.MONDAY
            )
        }
    }

    fun setSelectedDay(day: Int) {
        selectedDay = day
        invalidate()
    }

    fun setEventsOnMonth(events: List<CalendarDayState>) {
        val map = mutableMapOf<Calendar, List<EventInCalendarGrid>>()
        events.forEach { state ->

            map[state.day] = state.events
        }
        calendarDayStateList = map
        invalidate()
    }

    fun setMonthParams(year: Int, month: Int, selectedDay: Int, weekStart: Int) {
        yearNumber = year
        if (isValidMonth(month))
            monthNumber = month

        val cal = Calendar.getInstance()

        daysInMonth = getDaysInMonth(monthNumber, yearNumber)
        daysInPrevMonth = if (monthNumber - 1 == -1) {
            getDaysInMonth(Calendar.DECEMBER, yearNumber - 1)
        } else {
            getDaysInMonth(monthNumber - 1, yearNumber)
        }

        today = -1
        for (day in 1..daysInMonth) {
            if (isToday(day, cal)) {
                today = day
            }
        }

        calendar.set(Calendar.YEAR, yearNumber)
        calendar.set(Calendar.MONTH, monthNumber)
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        dayOfWeekStart = calendar.get(Calendar.DAY_OF_WEEK)

        this.selectedDay = selectedDay
        this.weekStart = weekStart

        invalidate()
    }

    private fun initAttributes(
        attributeSet: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        val typedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.MadCalendarMonth,
            defStyleAttr,
            defStyleRes
        )

        desiredCellWidth = resources.getDimensionPixelSize(R.dimen.calendar_cell_width)
        cellWidth = desiredCellWidth
        desiredCellHeight = resources.getDimensionPixelSize(R.dimen.calendar_cell_height)
        cellHeight = desiredCellHeight
        desiredDayOfWeekHeight =
            resources.getDimensionPixelSize(R.dimen.calendar_day_of_week_height)
        desiredTodaySquareWidth =
            resources.getDimensionPixelSize(R.dimen.calendar_today_square_width)

        desiredDayTitleTextSize =
            resources.getDimensionPixelSize(R.dimen.calendar_day_title_text_size).toFloat()
        desiredEventTitleTextSize =
            resources.getDimensionPixelSize(R.dimen.calendar_event_title_text_size).toFloat()
        desiredDayOfWeekTextSize =
            resources.getDimensionPixelSize(R.dimen.calendar_day_of_week_title_text_size).toFloat()

        daysOfWeekTitleColor = typedArray.getColor(
            R.styleable.MadCalendarMonth_mad_daysOfWeekTitleColor,
            ContextCompat.getColor(context, R.color.onyx)
        )
        daysOfWeekTitleTextSize = typedArray.getDimensionPixelSize(
            R.styleable.MadCalendarMonth_mad_daysOfWeekTitleTextSize,
            14
        ).toFloat()
        gridColor = typedArray.getColor(
            R.styleable.MadCalendarMonth_mad_gridColor,
            ContextCompat.getColor(context, R.color.dark_cloud)
        )
        dayTitleTextSize = typedArray.getDimensionPixelSize(
            R.styleable.MadCalendarMonth_mad_dayTitleTextSize,
            14
        ).toFloat()
        selectedDayColor = typedArray.getColor(
            R.styleable.MadCalendarMonth_mad_selectedDayColor,
            ContextCompat.getColor(context, R.color.dark_cloud)
        )
        currentDayColor = typedArray.getColor(
            R.styleable.MadCalendarMonth_mad_currentDayColor,
            ContextCompat.getColor(context, R.color.metallic_seaweed)
        )
        currentDayTitleColor = typedArray.getColor(
            R.styleable.MadCalendarMonth_mad_currentDayTitleColor,
            ContextCompat.getColor(context, R.color.mercury)
        )
        eventTextSize = typedArray.getDimensionPixelSize(
            R.styleable.MadCalendarMonth_mad_eventTextSize,
            14
        ).toFloat()
        eventTitleLightColor = typedArray.getColor(
            R.styleable.MadCalendarMonth_mad_eventTitleLightColor,
            ContextCompat.getColor(context, R.color.mercury)
        )
        eventTitleDarkColor = typedArray.getColor(
            R.styleable.MadCalendarMonth_mad_eventTitleDarkColor,
            ContextCompat.getColor(context, R.color.onyx)
        )
        currentMonthDaysColor = typedArray.getColor(
            R.styleable.MadCalendarMonth_mad_currentMonthDaysColor,
            ContextCompat.getColor(context, R.color.onyx)
        )
        otherMonthDaysColor = typedArray.getColor(
            R.styleable.MadCalendarMonth_mad_otherMonthDaysColor,
            ContextCompat.getColor(context, R.color.dark_cloud)
        )

        typedArray.recycle()
    }

    fun setOnDayClickListener(listener: OnDayClickListener) {
        onDayClickListener = listener
    }

    private fun initPaints() {
        val font = resources.getFont(R.font.blogger_sans_bold)

        daysOfWeekPaint.textSize =
            if (isInEditMode) desiredDayOfWeekTextSize else daysOfWeekTitleTextSize
        daysOfWeekPaint.typeface = font
        daysOfWeekPaint.textAlign = Paint.Align.CENTER
        daysOfWeekPaint.style = Paint.Style.FILL
        daysOfWeekPaint.color = daysOfWeekTitleColor

        currentMonthDaysPaint.textSize =
            if (isInEditMode) desiredDayTitleTextSize else dayTitleTextSize
        currentMonthDaysPaint.typeface = font
        currentMonthDaysPaint.textAlign = Paint.Align.CENTER
        currentMonthDaysPaint.style = Paint.Style.FILL
        currentMonthDaysPaint.color = currentMonthDaysColor

        todayTitlePaint.textSize =
            if (isInEditMode) desiredDayTitleTextSize else dayTitleTextSize
        todayTitlePaint.typeface = font
        todayTitlePaint.textAlign = Paint.Align.CENTER
        todayTitlePaint.style = Paint.Style.FILL
        todayTitlePaint.color = currentDayTitleColor

        otherMonthDaysPaint.textSize =
            if (isInEditMode) desiredDayTitleTextSize else dayTitleTextSize
        otherMonthDaysPaint.typeface = font
        otherMonthDaysPaint.textAlign = Paint.Align.CENTER
        otherMonthDaysPaint.style = Paint.Style.FILL
        otherMonthDaysPaint.color = otherMonthDaysColor

        eventTextPaint.textSize = if (isInEditMode) desiredEventTitleTextSize else eventTextSize
        eventTextPaint.typeface = font
        eventTextPaint.textAlign = Paint.Align.CENTER
        eventTextPaint.style = Paint.Style.FILL
        eventTextPaint.color = currentMonthDaysColor

        eventBackgroundPaint.style = Paint.Style.FILL

        selectedDayPaint.color = selectedDayColor
        selectedDayPaint.strokeWidth = selectedDayWidth
        selectedDayPaint.style = Paint.Style.STROKE

        currentDayHighlightPaint.color = currentDayColor
        currentDayHighlightPaint.style = Paint.Style.FILL

        gridPaint.color = gridColor
        gridPaint.strokeWidth = gridWidth
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGrid(canvas)
        drawDayOfWeek(canvas)
        drawDaysTitle(canvas)
        drawEvents(canvas)
    }

    private fun drawGrid(canvas: Canvas) {
        val xStart = paddingLeft.toFloat()
        val xEnd = (paddingLeft + paddedWidth).toFloat()
        for (i in 0..WEEKS_IN_MONTH) {
            val y = (dayOfWeekHeight + paddingTop + cellHeight * i).toFloat()
            canvas.drawLine(xStart, y, xEnd, y, gridPaint)
        }
        val yStart = paddingTop.toFloat() + dayOfWeekHeight / 2
        val yEnd = (paddingTop + paddedHeight).toFloat()
        for (i in 0..DAYS_IN_WEEK) {
            val x = (paddingLeft + cellWidth * i).toFloat()
            canvas.drawLine(x, yStart, x, yEnd, gridPaint)
        }
    }

    private fun drawDayOfWeek(canvas: Canvas) {
        val halfLineHeight = (daysOfWeekPaint.ascent() + daysOfWeekPaint.descent()) / 2f
        val rowCenter = dayOfWeekHeight / 2

        for (i in 0 until DAYS_IN_WEEK) {
            val colCenter = cellWidth * i + cellWidth / 2f
            canvas.drawText(
                dayOfWeekLabels[i],
                colCenter,
                rowCenter - halfLineHeight,
                daysOfWeekPaint
            )
        }
    }

    private fun getDayByTouch(x: Int, y: Int): Int {
        val row: Int = (y - paddingTop - dayOfWeekHeight) / cellHeight
        val col: Int = (x - paddingLeft) * DAYS_IN_WEEK / paddedWidth
        val index: Int = col + row * DAYS_IN_WEEK
        return index + 1 - findDayOffset()
    }

    private fun drawDaysTitle(canvas: Canvas) {
        val startHeight = dayOfWeekHeight

        val halfLineHeight = (currentMonthDaysPaint.ascent() + currentMonthDaysPaint.descent()) / 2f
        var dayTitleRowCenter = startHeight + todaySquareTopLeftPoint.y + todaySquareWidth / 2

        var col = findDayOffset()

        for (day in 1..daysInMonth) {
            val colCenter = cellWidth * col + cellWidth / 2
            if (day == today) {
                canvas.drawRoundRect(
                    (colCenter - todaySquareWidth / 2).toFloat(),
                    dayTitleRowCenter - todaySquareWidth / 2 - 5,
                    (colCenter + todaySquareWidth / 2).toFloat(),
                    dayTitleRowCenter + todaySquareWidth / 2,
                    15f,
                    15f,
                    currentDayHighlightPaint
                )
                canvas.drawText(
                    day.toString(),
                    colCenter.toFloat(),
                    dayTitleRowCenter - halfLineHeight,
                    todayTitlePaint
                )
            } else {
                canvas.drawText(
                    day.toString(),
                    colCenter.toFloat(),
                    dayTitleRowCenter - halfLineHeight,
                    currentMonthDaysPaint
                )
            }
            if (day == selectedDay) {
                canvas.drawRoundRect(
                    (colCenter - cellWidth / 2 + selectedDayBorderPadding).toFloat(),
                    dayTitleRowCenter - todaySquareWidth / 2 - todaySquareTopLeftPoint.y + selectedDayBorderPadding,
                    (colCenter + cellWidth / 2 - selectedDayBorderPadding).toFloat(),
                    dayTitleRowCenter +
                            todaySquareWidth / 2 +
                            eventsRect.height(),
                    15f,
                    15f,
                    selectedDayPaint
                )
            }
            col++
            if (col >= DAYS_IN_WEEK) {
                dayTitleRowCenter += cellHeight
                col = 0
            }
        }
        val endNextMonth = if (dayTitleRowCenter + cellHeight > height) {
            DAYS_IN_WEEK - col
        } else {
            DAYS_IN_WEEK - col + DAYS_IN_WEEK
        }
        for (day in 1..endNextMonth) {
            val colCenter = cellWidth * col + cellWidth / 2
            canvas.drawText(
                day.toString(),
                colCenter.toFloat(),
                dayTitleRowCenter - halfLineHeight,
                otherMonthDaysPaint
            )
            col++
            if (col >= DAYS_IN_WEEK) {
                dayTitleRowCenter += cellHeight
                col = 0
            }
        }
        col = 0
        dayTitleRowCenter = startHeight + todaySquareTopLeftPoint.y + todaySquareWidth / 2
        for (day in daysInPrevMonth - findDayOffset() + 1..daysInPrevMonth) {
            val colCenter = cellWidth * col + cellWidth / 2
            canvas.drawText(
                day.toString(),
                colCenter.toFloat(),
                dayTitleRowCenter - halfLineHeight,
                otherMonthDaysPaint
            )
            col++
        }
    }

    private fun drawEvents(canvas: Canvas) {
        val startHeight = dayOfWeekHeight
        var eventBackgroundHeight: Int
        var staticLayout: StaticLayout

        calendarDayStateList.keys.forEach { day ->
            var eventStartHeight = startHeight + eventsRect.top
            val col = getColByDate(day)
            val row = getRowByDate(day)
            eventStartHeight += (cellHeight * row)
            val left = col * cellWidth + eventsRect.left
            val right = left + eventsRect.width()
            calendarDayStateList[day]?.forEachIndexed { index, event ->
                eventBackgroundPaint.color = event.color
                eventTextPaint.color = getContrastRatioColor(eventBackgroundPaint.color)
                val text = event.title
                staticLayout = StaticLayout
                    .Builder
                    .obtain(
                        text,
                        0,
                        text.length,
                        eventTextPaint,
                        (eventsRect.width() - 5).toInt()
                    )
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setEllipsize(TextUtils.TruncateAt.END)
                    .setMaxLines(1)
                    .build()
                eventBackgroundHeight = staticLayout.height + 10
                val top = eventStartHeight + index * eventBackgroundHeight
                if (top + eventBackgroundHeight <= eventStartHeight + eventsRect.height()) {
                    canvas.drawRoundRect(
                        left,
                        top + 5,
                        right,
                        top + eventBackgroundHeight,
                        10f,
                        10f,
                        eventBackgroundPaint
                    )
                    canvas.save()
                    canvas.translate(left + 5 + staticLayout.width / 2f, top + 5)
                    staticLayout.draw(canvas)
                    canvas.restore()
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = (event.x + 0.5f).toInt()
        val y = (event.y + 0.5f).toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val touchedItem = getDayByTouch(x, y)
                if (selectedDay != touchedItem) {
                    selectedDay = touchedItem
                    invalidate()
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                return performClick()
            }
        }
        return false
    }

    override fun performClick(): Boolean {
        val date = Calendar.getInstance().apply {
            set(Calendar.YEAR, yearNumber)
            set(Calendar.MONTH, monthNumber)
            set(Calendar.DAY_OF_MONTH, selectedDay)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            set(Calendar.MILLISECONDS_IN_DAY, 0)
        }
        onDayClickListener?.onDayClick(
            this,
            date,
            calendarDayStateList[date] ?: emptyList()
        )
        return super.performClick()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (!changed) {
            return
        }

        val w = right - left
        val h = bottom - top

        val paddedWidth = w - paddingRight - paddingLeft
        val paddedHeight = h - paddingBottom - paddingTop

        if (paddedWidth == this.paddedWidth || paddedHeight == this.paddedHeight) {
            return
        }

        this.paddedWidth = paddedWidth
        this.paddedHeight = paddedHeight

        val measuredPaddedHeight = measuredHeight - paddingTop - paddingBottom
        val scaleH = paddedHeight / measuredPaddedHeight.toFloat()
        val cellWidth: Int = this.paddedWidth / DAYS_IN_WEEK
        dayOfWeekHeight = (desiredDayOfWeekHeight * scaleH).toInt()
        val cellHeight: Int = (this.paddedHeight - dayOfWeekHeight) / WEEKS_IN_MONTH
        this.cellWidth = cellWidth
        this.cellHeight = cellHeight

        todaySquareWidth =
            (currentMonthDaysPaint.descent() - currentMonthDaysPaint.ascent() + 20).toInt()

        todaySquareTopLeftPoint.x = cellWidth / 2f - todaySquareWidth / 2
        todaySquareTopLeftPoint.y = cellHeight * 0.1f

        eventsRect.left = selectedDayBorderPadding + selectedDayWidth
        eventsRect.top = todaySquareTopLeftPoint.y + todaySquareWidth
        eventsRect.right = cellWidth - selectedDayBorderPadding - selectedDayWidth
        eventsRect.bottom = cellHeight - selectedDayBorderPadding - selectedDayWidth
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val preferredHeight: Int = desiredDayOfWeekHeight + desiredCellHeight * WEEKS_IN_MONTH +
                paddingTop + paddingBottom
        val preferredWidth: Int = desiredCellWidth * DAYS_IN_WEEK + paddingStart + paddingEnd
        val resolvedWidth = resolveSize(preferredWidth, widthMeasureSpec)
        val resolvedHeight = resolveSize(preferredHeight, heightMeasureSpec)

        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }

    private fun isToday(day: Int, cal: Calendar): Boolean {
        return monthNumber == cal.get(Calendar.MONTH) &&
                yearNumber == cal.get(Calendar.YEAR) &&
                day == cal.get(Calendar.DAY_OF_MONTH)
    }

    private fun getDaysInMonth(month: Int, year: Int): Int {
        return when (month) {
            Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER, Calendar.DECEMBER -> 31
            Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> 30
            Calendar.FEBRUARY -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
            else -> -1
        }
    }

    private fun getColByDate(date: Calendar): Int {
        return when (date.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> 6
            Calendar.MONDAY -> 0
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            Calendar.SATURDAY -> 5
            else -> 0
        }
    }

    private fun getRowByDate(date: Calendar): Int {
        val localeDate = Calendar.getInstance(timeZone, locale).apply {
            timeInMillis = date.timeInMillis
            firstDayOfWeek = Calendar.MONDAY
        }
        return if (localeDate.get(Calendar.MONTH) < monthNumber)
            0
        else if (localeDate.get(Calendar.MONTH) > monthNumber)
            localeDate.get(Calendar.WEEK_OF_MONTH) - 1 + localeDate.apply {
                add(Calendar.MONTH, -1)
            }.get(WEEKS_IN_MONTH)
        else
            localeDate.get(Calendar.WEEK_OF_MONTH) - 1
    }

    private fun findDayOffset(): Int {
        val offset: Int = dayOfWeekStart - weekStart
        return if (dayOfWeekStart < weekStart) {
            offset + DAYS_IN_WEEK
        } else offset
    }

    private fun getContrastRatioColor(
        backgroundColor: Int
    ): Int {
        return if (ColorUtils.calculateContrast(eventTitleLightColor, backgroundColor) >
            ColorUtils.calculateContrast(eventTitleDarkColor, backgroundColor)
        ) {
            eventTitleLightColor
        } else {
            eventTitleDarkColor
        }
    }

    private fun isValidMonth(month: Int): Boolean {
        return (month >= Calendar.JANUARY && month <= Calendar.DECEMBER)
    }

    interface OnDayClickListener {
        fun onDayClick(view: MadCalendarMonth, day: Calendar, events: List<EventInCalendarGrid>)
    }

    private companion object {
        const val DAYS_IN_WEEK = 7
        const val WEEKS_IN_MONTH = 6
        const val DEFAULT_SELECTED_DAY = -1
        const val DEFAULT_WEEK_START = Calendar.MONDAY
    }

}