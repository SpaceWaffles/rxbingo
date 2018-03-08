package io.spacenoodles.rxbingo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import io.spacenoodles.rxbingo.R

class BingoView : View {

    var name: String = ""
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    var gridColor = DEFAULT_GRID_COLOR
        set(gridColor) {
            field = gridColor
            invalidate()
        }

    var titleColor = DEFAULT_TITLE_COLOR
        set(titleColor) {
            field = titleColor
            invalidate()
        }

    var numberColor = DEFAULT_NUMBER_COLOR
        set(numberColor) {
            field = numberColor
            invalidate()
        }

    var nameColor = DEFAULT_NAME_COLOR
        set(nameColor) {
            field = nameColor
            invalidate()
        }

    var markedSquareColor = DEFAULT_MARKED_SQUARE_COLOR
        set(markedSquareColor) {
            field = markedSquareColor
            invalidate()
        }

    private var numberRect = Rect()

    private val boardSize = 450f
    private val textPadding = 10f

    private lateinit var linePaint: Paint
    private lateinit var textPaint: Paint
    private lateinit var textNumberPaint: Paint
    private lateinit var markedSquarePaint: Paint
    private lateinit var namePaint: Paint

    private val lineStrokeWidth = 4f

    private val textYOffset = 90f
    private val bottomTextYOffset = 70f

    private var squares: Array<Array<BingoSquare>> = Array(5) { Array(5){ BingoSquare(0, 0, 0) }}

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }


    private fun init(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs,
                R.styleable.bingo_board_attrs)

        gridColor = a.getColor(R.styleable.bingo_board_attrs_grid_color, DEFAULT_GRID_COLOR)
        titleColor = a.getColor(R.styleable.bingo_board_attrs_title_color, DEFAULT_TITLE_COLOR)
        numberColor = a.getColor(R.styleable.bingo_board_attrs_number_color, DEFAULT_NUMBER_COLOR)
        nameColor = a.getColor(R.styleable.bingo_board_attrs_name_color, DEFAULT_NAME_COLOR)
        markedSquareColor = a.getColor(R.styleable.bingo_board_attrs_marked_square_color, DEFAULT_MARKED_SQUARE_COLOR)

        linePaint = Paint()
        linePaint.color = gridColor
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = lineStrokeWidth
        linePaint.isAntiAlias = true

        textPaint = Paint()
        textPaint.color = titleColor
        textPaint.textSize = 42f * resources.displayMetrics.density
        textPaint.isAntiAlias = true

        textNumberPaint = Paint()
        textNumberPaint.color = numberColor
        textNumberPaint.textSize = 26f * resources.displayMetrics.density
        textNumberPaint.isAntiAlias = true

        markedSquarePaint = Paint()
        markedSquarePaint.color = markedSquareColor
        markedSquarePaint.style = Paint.Style.FILL
        markedSquarePaint.isAntiAlias = true

        namePaint = Paint()
        namePaint.color = nameColor
        namePaint.textSize = 24f * resources.displayMetrics.density
        namePaint.isAntiAlias = true

        a.recycle()
    }

    fun setSquares(squares: Array<Array<BingoSquare>>) {
        this.squares = squares
        invalidate()
        requestLayout()
    }

    fun setPlayerName(name: String) {
        this.name = name
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw = (boardSize + paddingLeft + paddingRight).toInt()
        val w = resolveSizeAndState(minw, widthMeasureSpec, 0)

        val minh = (boardSize + paddingBottom + paddingTop + textPadding + textYOffset + bottomTextYOffset).toInt()
        val h = resolveSizeAndState(minh, heightMeasureSpec, 0)

        setMeasuredDimension(w, h)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val boardSectionSize = boardSize / 5

        canvas.drawText("B I N G O", textPadding, textYOffset, textPaint)
        canvas.drawText(name, textPadding, textYOffset + boardSize + bottomTextYOffset, namePaint)

        squares.forEach {
            it.forEach {
                if (it.marked) {
                    drawMarkedSquare(canvas, markedSquarePaint, it.column, it.row, textYOffset, boardSectionSize, lineStrokeWidth, textPadding)
                }
                if (it.number != 0) {
                    numberRect.left = 0
                    numberRect.top = (textYOffset + textPadding).toInt()
                    numberRect.bottom = (boardSectionSize + textYOffset + textPadding).toInt()
                    numberRect.right = (boardSectionSize).toInt()
                    drawNumber(canvas, textNumberPaint, it.number.toString(), it.column, it.row, boardSectionSize, textPadding, lineStrokeWidth)
                }
            }
        }

        //Draw grid
        canvas.drawRect(0f, textYOffset + textPadding, boardSize, boardSize + textYOffset + textPadding, linePaint)

        canvas.drawLine(0f, boardSectionSize + textYOffset + textPadding, boardSize, boardSectionSize + textYOffset + textPadding, linePaint)
        canvas.drawLine(0f, boardSectionSize * 2 + textYOffset + textPadding, boardSize, boardSectionSize * 2 + textYOffset + textPadding, linePaint)
        canvas.drawLine(0f, boardSectionSize * 3 + textYOffset + textPadding, boardSize, boardSectionSize * 3 + textYOffset + textPadding, linePaint)
        canvas.drawLine(0f, boardSectionSize * 4 + textYOffset + textPadding, boardSize, boardSectionSize * 4 + textYOffset + textPadding, linePaint)

        canvas.drawLine(boardSectionSize + lineStrokeWidth, textYOffset + textPadding, boardSectionSize + lineStrokeWidth, boardSize + textYOffset + textPadding, linePaint)
        canvas.drawLine(boardSectionSize * 2 + lineStrokeWidth, textYOffset + textPadding, boardSectionSize * 2 + lineStrokeWidth, boardSize + textYOffset + textPadding, linePaint)
        canvas.drawLine(boardSectionSize * 3 + lineStrokeWidth, textYOffset + textPadding, boardSectionSize * 3 + lineStrokeWidth, boardSize + textYOffset + textPadding, linePaint)
        canvas.drawLine(boardSectionSize * 4 + lineStrokeWidth, textYOffset + textPadding, boardSectionSize * 4 + lineStrokeWidth, boardSize + textYOffset + textPadding, linePaint)

        //Draw X on free space
        canvas.drawLine(boardSectionSize * 2 + lineStrokeWidth, boardSectionSize * 2 + textYOffset + textPadding, boardSectionSize * 3 + lineStrokeWidth, boardSectionSize * 3 + textYOffset + textPadding, linePaint)
        canvas.drawLine(boardSectionSize * 3 + lineStrokeWidth, boardSectionSize * 2 + textYOffset + textPadding, boardSectionSize * 2 + lineStrokeWidth, boardSectionSize * 3 + textYOffset + textPadding, linePaint)
    }

    private fun drawNumber(canvas: Canvas, paint: Paint, text: String, posX: Int, posY: Int, boardSectionSize: Float, textPadding: Float, strokeWidth: Float) {
        paint.textAlign = Paint.Align.LEFT
        val height = numberRect.height()
        val width = numberRect.width()
        paint.getTextBounds(text, 0, text.length, numberRect)
        val x = width / 2f - numberRect.width() / 2f - numberRect.left
        val y = height / 2f + numberRect.height() / 2f - numberRect.bottom
        canvas.drawText(text, x + (posX * boardSectionSize) + strokeWidth / 2, y + (boardSectionSize) + (posY * boardSectionSize) + textPadding, paint)
    }

    private fun drawMarkedSquare(canvas: Canvas, paint: Paint, posX: Int, posY: Int, textYOffset: Float, boardSectionSize: Float, strokeWidth: Float, textPadding: Float) {
        canvas.drawRect(posX * boardSectionSize + strokeWidth, posY * boardSectionSize + textYOffset + textPadding, (posX + 1) * boardSectionSize + strokeWidth, (posY + 1) * boardSectionSize + textYOffset + textPadding, paint)
    }

    companion object {
        private val DEFAULT_GRID_COLOR = Color.BLACK
        private val DEFAULT_TITLE_COLOR = Color.BLUE
        private val DEFAULT_NUMBER_COLOR = Color.BLACK
        private val DEFAULT_NAME_COLOR = Color.GRAY
        private val DEFAULT_MARKED_SQUARE_COLOR = Color.GREEN
    }

    data class BingoSquare(var number: Int, val row: Int, val column: Int, var marked: Boolean = false)
}