package io.spacenoodles.rxbingo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import io.spacenoodles.rxbingo.R

class CircleView : View {
    var circleColor = DEFAULT_CIRCLE_COLOR
        set(circleColor) {
            field = circleColor
            invalidate()
        }
    var strokeColor = DEFAULT_STROKE_COLOR
        set(strokeColor) {
            field = strokeColor
            invalidate()
        }
    var strokeWidth = DEFAULT_STROKE_WIDTH
        set(strokeWidth) {
            field = strokeWidth
            invalidate()
        }
    private lateinit var paint: Paint

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        paint = Paint()
        paint.isAntiAlias = true

        val a = context.obtainStyledAttributes(attrs,
                R.styleable.circle_view_attrs)

        circleColor = a.getColor(R.styleable.circle_view_attrs_fill_color, Color.BLACK)
        strokeColor = a.getColor(R.styleable.circle_view_attrs_stroke_color, Color.BLACK)
        strokeWidth = a.getDimension(R.styleable.circle_view_attrs_stroke_width, 0f)

        a.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width
        val h = height

        val pl = paddingLeft
        val pr = paddingRight
        val pt = paddingTop
        val pb = paddingBottom

        val usableWidth = w - (pl + pr)
        val usableHeight = h - (pt + pb)

        val radius = Math.min(usableWidth, usableHeight) / 2
        val cx = pl + usableWidth / 2
        val cy = pt + usableHeight / 2

        paint.color = this.circleColor

        fillCircleStrokeBorder(canvas, cx.toFloat(), cy.toFloat(), radius.toFloat())
    }

    private fun fillCircleStrokeBorder(
            c: Canvas, cx: Float, cy: Float, radius: Float) {

        val saveColor = paint.color
        paint.color = this.circleColor
        val saveStyle = paint.style
        paint.style = Paint.Style.FILL
        c.drawCircle(cx, cy, radius, paint)
        if (this.strokeWidth > 0) {
            paint.color = this.strokeColor
            paint.style = Paint.Style.STROKE
            val saveStrokeWidth = paint.strokeWidth
            paint.strokeWidth = this.strokeWidth
            c.drawCircle(cx, cy, radius - this.strokeWidth / 2, paint)
            paint.strokeWidth = saveStrokeWidth
        }
        paint.color = saveColor
        paint.style = saveStyle
    }

    companion object {
        private val DEFAULT_CIRCLE_COLOR = Color.RED
        private val DEFAULT_STROKE_COLOR = Color.BLACK
        private val DEFAULT_STROKE_WIDTH = 0f
    }
}
