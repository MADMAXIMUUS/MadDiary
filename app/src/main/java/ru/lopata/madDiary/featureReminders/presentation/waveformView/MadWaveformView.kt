package ru.lopata.madDiary.featureReminders.presentation.waveformView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import ru.lopata.madDiary.R

class MadWaveformView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var waveLineWidth = 0f
    private var waveLineColor = 0
    private val amplitude = mutableListOf<Float>()
    private var amps = emptyList<Float>()
    private val d = 6f

    private var sw = 0f
    private var sh = 400f

    private var paddedWidth = 0
    private var paddedHeight = 0

    private var maxSpikes = 0

    init {
        if (attrs != null) {
            initAttributes(attrs, defStyleAttr, defStyleAttr)
            initPaint()
        }
    }

    private fun initAttributes(
        attributeSet: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {

        val typedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.MadWaveformView,
            defStyleAttr,
            defStyleRes
        )

        waveLineColor = typedArray.getColor(
            R.styleable.MadWaveformView_mad_waveLineColor,
            ContextCompat.getColor(context, R.color.metallic_seaweed)
        )
        waveLineWidth = typedArray.getDimensionPixelSize(
            R.styleable.MadWaveformView_mad_waveLineWidth,
            resources.getDimensionPixelSize(R.dimen.wave_line_width)
        ).toFloat()

        typedArray.recycle()
    }

    private fun initPaint() {
        linePaint.style = Paint.Style.STROKE
        linePaint.color = waveLineColor
        linePaint.strokeWidth = waveLineWidth
        linePaint.strokeCap = Paint.Cap.ROUND
    }

    fun clear() {
        amplitude.clear()
        amps = emptyList()
        invalidate()
        requestLayout()
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

        sh = (measuredHeight - paddingTop - paddingBottom).toFloat()
        sw = (measuredWidth - paddingRight - paddingLeft).toFloat()
        maxSpikes = (sw / (waveLineWidth + d)).toInt()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        sh = h.toFloat()
        sw = w.toFloat()
        requestLayout()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val preferredHeight: Int = widthMeasureSpec
        val preferredWidth: Int = heightMeasureSpec
        val resolvedWidth = resolveSize(preferredWidth, widthMeasureSpec)
        val resolvedHeight = resolveSize(preferredHeight, heightMeasureSpec)

        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }

    fun addAmplitude(amp: Float) {
        amplitude.add(amp / 5)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        amps = amplitude.takeLast(maxSpikes)
        for (i in amps.indices) {
            val x = i * (waveLineWidth + d)
            val y1 = sh / 2 - amps[i] / 2
            val y2 = sh / 2 + amps[i] / 2
            canvas.drawLine(x, y1, x, y2, linePaint)
        }
    }
}