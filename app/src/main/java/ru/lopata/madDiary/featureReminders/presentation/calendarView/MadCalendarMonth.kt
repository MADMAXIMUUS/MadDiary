package ru.lopata.madDiary.featureReminders.presentation.calendarView

import android.content.Context
import android.graphics.*
import android.icu.util.Calendar
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
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.util.DayType
import java.text.NumberFormat
import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.floor

class MadCalendarMonth @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var events: List<List<Event>> = emptyList()

    private var monthNumber = 1
    private var yearNumber = 1970

    private val daysOfWeekPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val currentMonthDaysPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val todayTitlePaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val otherMonthDaysPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val eventTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val selectedDayPaint = Paint()
    private val eventBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val currentDayHighlightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val gridPaint = Paint()

    private val dayOfWeekLabels: Array<String> = resources.getStringArray(R.array.days_in_week)

    private val locale = resources.configuration.locales.get(0)
    private val calendar = Calendar.getInstance(locale)

    private var dayTitleFormatter = NumberFormat.getIntegerInstance(locale)

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
    private var today = -1
    private var weekStart = DEFAULT_WEEK_START
    private val dayOfWeekStart = 0
    private var daysInMonth = 31
    private var paddedWidth = 0
    private var paddedHeight = 0

    private var daysOfWeekTitleColor = 0
    private var daysOfWeekTitleTextSize = 0f
    private var gridColor = 0
    private var gridWidth = 1f
    private var dayTitleTextSize = 0f
    private var selectedDayColor = 0
    private var selectedDayWidth = 1f
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
        if (isInEditMode) {

        }
        /*month = Month(
            YearMonth.of(2022, 10),
            days = listOf(
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 9, 26)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 9, 27)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 9, 28)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 9, 29)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 9, 30)
                ),
                CalendarDate(
                    dateType = DayType.TODAY,
                    day = LocalDate.of(2022, 10, 1)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 2)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 3)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 4)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 5)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 6),
                    events = listOf(
                        Event(
                            title = "Купить продукты",
                            color = Color.RED
                        ),
                        Event(
                            title = "Купить еду",
                            color = Color.GREEN
                        )
                    )
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 7),
                    events = listOf(
                        Event(
                            title = "Купить продукты",
                            color = Color.BLUE
                        ),
                        Event(
                            title = "Купить еду",
                            color = Color.MAGENTA
                        ),
                        Event(
                            title = "Купить еду",
                            color = Color.YELLOW
                        ),
                        Event(
                            title = "Купить еду",
                            color = Color.YELLOW
                        ),
                        Event(
                            title = "Купить еду",
                            color = Color.YELLOW
                        ),
                        Event(
                            title = "Купить еду",
                            color = Color.YELLOW
                        ),
                        Event(
                            title = "Купить еду",
                            color = Color.YELLOW
                        ),
                        Event(
                            title = "Купить еду",
                            color = Color.YELLOW
                        ),
                        Event(
                            title = "Купить еду",
                            color = Color.YELLOW
                        )
                    )
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 8)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 9)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 10)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 11)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 12)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 13)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 14)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 15)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 16)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 17)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 18)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 19)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 20)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 21)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 22)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 23)
                ),
                CalendarDate(
                    dateType = DayType.TODAY,
                    day = LocalDate.of(2022, 10, 24)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 25)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 26)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 27)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 28)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 29)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 30)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 31)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 11, 1)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 11, 2)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 11, 3)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 11, 4)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 11, 5)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 11, 6)
                ),
            )
        )*/
    }

    fun setEventsOnMonth(events: List<List<Event>>) {
        this.events = events
        requestLayout()
        invalidate()
    }

    fun getEventsOnMonth(): List<List<Event>> {
        return events
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

        currentDayHighlightPaint.color = currentDayColor
        currentDayHighlightPaint.style = Paint.Style.FILL

        gridPaint.color = gridColor
        gridPaint.strokeWidth = gridWidth
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
        drawGrid(canvas)
        drawDayOfWeek(canvas)
        drawDaysTitle(canvas)
        drawEvents(canvas)
        canvas.translate(-paddingLeft.toFloat(), -paddingTop.toFloat())
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

    private fun drawDaysTitle(canvas: Canvas) {
        /*val days = month?.days ?: return
        val startHeight = dayOfWeekHeight

        val halfLineHeight = (currentMonthDaysPaint.ascent() + currentMonthDaysPaint.descent()) / 2f
        var dayTitleRowCenter = startHeight + todaySquareTopLeftPoint.y + todaySquareWidth / 2

        var col = 0

        for (i in days.indices) {
            val colCenter = cellWidth * col + cellWidth / 2
            if (days[i].dateType == DayType.TODAY) {
                canvas.drawRoundRect(
                    (colCenter - todaySquareWidth / 2).toFloat(),
                    dayTitleRowCenter - todaySquareWidth / 2 - 5,
                    (colCenter + todaySquareWidth / 2).toFloat(),
                    dayTitleRowCenter + todaySquareWidth / 2,
                    15f,
                    15f,
                    currentDayHighlightPaint
                )
            }
            when (days[i].dateType) {
                DayType.CURRENT_MONTH -> {
                    canvas.drawText(
                        days[i].day.dayOfMonth.toString(),
                        colCenter.toFloat(),
                        dayTitleRowCenter - halfLineHeight,
                        currentMonthDaysPaint
                    )
                }
                DayType.TODAY -> {
                    canvas.drawText(
                        days[i].day.dayOfMonth.toString(),
                        colCenter.toFloat(),
                        dayTitleRowCenter - halfLineHeight,
                        todayTitlePaint
                    )
                }
                else -> {
                    canvas.drawText(
                        days[i].day.dayOfMonth.toString(),
                        colCenter.toFloat(),
                        dayTitleRowCenter - halfLineHeight,
                        otherMonthDaysPaint
                    )
                }
            }
            col++
            if (col == DAYS_IN_WEEK) {
                dayTitleRowCenter += cellHeight
                col = 0
            }
        }*/
    }

    private fun drawEvents(canvas: Canvas) {
        /*val days = month?.days ?: return
        val startHeight = dayOfWeekHeight

        var eventStartHeight = startHeight + eventsRect.top
        var eventBackgroundHeight: Int
        var col = 0

        var staticLayout: StaticLayout

        for (i in days.indices) {
            val left = col * cellWidth + eventsRect.left
            val right = left + eventsRect.width()
            for (j in days[i].events.indices) {
                eventBackgroundPaint.color = days[i].events[j].color
                eventTextPaint.color = getContrastRatioColor(
                    eventTextPaint.color,
                    eventBackgroundPaint.color
                )
                val text = days[i].events[j].title
                staticLayout = StaticLayout
                    .Builder
                    .obtain(
                        text,
                        0,
                        text.length,
                        eventTextPaint,
                        rectWidth - 5
                    )
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setEllipsize(TextUtils.TruncateAt.END)
                    .setMaxLines(1)
                    .build()
                eventBackgroundHeight = staticLayout.height + 10
                val top = eventStartHeight + j * eventBackgroundHeight
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
            col++
            if (col == DAYS_IN_WEEK) {
                eventStartHeight += cellHeight
                col = 0
            }
        }*/
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (!changed) {
            return
        }

        val w = right - left
        val h = bottom - top

        val paddedRight = w - paddingRight
        val paddedBottom = h - paddingBottom

        val paddedWidth = paddedRight - paddingLeft
        val paddedHeight = paddedBottom - paddingTop

        if (paddedWidth == this.paddedWidth || paddedHeight == this.paddedHeight) {
            return
        }

        this.paddedWidth = paddedWidth
        this.paddedHeight = paddedHeight

        val measuredPaddedHeight = measuredHeight - paddingTop - paddingBottom
        val scaleH = paddedHeight / measuredPaddedHeight.toFloat()
        val cellWidth: Int = this.paddedWidth / DAYS_IN_WEEK
        val cellHeight: Int = this.paddedHeight / WEEKS_IN_MONTH
        dayOfWeekHeight = (desiredDayOfWeekHeight * scaleH).toInt()
        this.cellWidth = cellWidth
        this.cellHeight = cellHeight

        todaySquareWidth =
            (currentMonthDaysPaint.descent() - currentMonthDaysPaint.ascent() + 20).toInt()

        todaySquareTopLeftPoint.x = cellWidth / 2f - todaySquareWidth / 2
        todaySquareTopLeftPoint.y = cellHeight * 0.1f

        eventsRect.left = cellWidth * 0.01f
        eventsRect.top = todaySquareTopLeftPoint.y + todaySquareWidth
        eventsRect.right = cellWidth - cellWidth * 0.01f
        eventsRect.bottom = cellHeight - cellHeight * 0.01f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val preferredHeight: Int = desiredDayOfWeekHeight + desiredCellHeight * WEEKS_IN_MONTH +
                paddingTop + paddingBottom
        val preferredWidth: Int = desiredCellWidth * DAYS_IN_WEEK + paddingStart + paddingEnd
        val resolvedWidth = resolveSize(preferredWidth, widthMeasureSpec)
        val resolvedHeight = resolveSize(preferredHeight, heightMeasureSpec)

        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }

    private fun getDaysInMonth(month: Int, year: Int): Int {
        return when (month) {
            Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER, Calendar.DECEMBER -> 31
            Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> 30
            Calendar.FEBRUARY -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
            else -> -1
        }
    }

    private fun findDayOffset(): Int {
        val offset: Int = dayOfWeekStart - weekStart
        return if (dayOfWeekStart < weekStart) {
            offset + DAYS_IN_WEEK
        } else offset
    }

    private fun getTextTrunkedWidth(
        textWidthInPixel: Int,
        rectWidth: Int
    ): Int {
        return if (textWidthInPixel < rectWidth)
            textWidthInPixel
        else rectWidth - 5
    }

    private fun getContrastRatioColor(
        textColor: Int,
        backgroundColor: Int
    ): Int {
        return if (ColorUtils.calculateContrast(
                textColor, backgroundColor
            ) > 1.5f
        ) {
            eventTitleLightColor
        } else {
            eventTitleDarkColor
        }
    }

    private companion object {

        const val DAYS_IN_WEEK = 7
        const val WEEKS_IN_MONTH = 6
        const val DEFAULT_SELECTED_DAY = -1
        const val DEFAULT_WEEK_START = Calendar.MONDAY
    }

}