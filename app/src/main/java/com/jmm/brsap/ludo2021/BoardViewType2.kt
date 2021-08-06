package com.jmm.brsap.ludo2021

import android.content.Context
import android.graphics.*
import android.graphics.Shader.TileMode
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat


class BoardViewType2(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    // Painter
    private lateinit var borderPaint: Paint
    private lateinit var redPaint: Paint
    private lateinit var whitePaint: Paint
    private lateinit var greenPaint: Paint
    private lateinit var yellowPaint: Paint
    private lateinit var bluePaint: Paint


    // dimension of single cell into ludo map
    private var d: Float = 0f

    // screen width and height
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    // top and bottom padding to create 15x15 square map
    private var topSpacing: Int = 0
    private var bottomSpacing: Int = 0

    // cell padding
    private var cellPadding: Float = 0f

    // room padding
    private var restingRoomPadding: Float = 0f

    // tile radius
    private var tileRadius: Float = 0f

    // resting place radius
    private var restingPlaceRadius: Float = 0f

    // screen matrix
    private val _matrix = mutableListOf<MutableList<Pair<Float, Float>>>()
    private val column = mutableListOf<Float>()
    private val row = mutableListOf<Float>()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        prepareScreenDimensions()
        populateDisplayMatrix()
        initializePainters()
        createBoard(canvas)
        createPlayersSpot(canvas)

    }

    private fun prepareScreenDimensions() {
        mWidth = width
        mHeight = height

        d = (width / 15).toFloat()
        /*
        * This for making map 15x15 square shaped
        * */
        topSpacing = (mHeight - mWidth) / 2
        bottomSpacing = (mHeight + mWidth) / 2

        cellPadding = d / 15
        tileRadius = d / 10
        restingRoomPadding = d / 5
        restingPlaceRadius = d / 3


    }

    private fun initializePainters() {
        borderPaint = Paint()

        borderPaint.apply {
            color = Color.BLACK
            strokeWidth = 2f
            style = Paint.Style.STROKE
            isAntiAlias = false
        }


        redPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        redPaint.apply {
            color = Color.RED
            isAntiAlias = true
            style = Paint.Style.STROKE
        }

        whitePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        whitePaint.apply {
            color = Color.WHITE
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        bluePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bluePaint.apply {
            color = Color.BLUE
            isAntiAlias = true
            style = Paint.Style.STROKE
        }

        yellowPaint = Paint()
        yellowPaint.apply {
            color = Color.YELLOW
            isAntiAlias = true
            style = Paint.Style.STROKE
        }

        greenPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        greenPaint.apply {
            strokeWidth= 5F
            color = Color.GREEN
            isAntiAlias = true
            style = Paint.Style.STROKE
        }

//        greenPaint.setColor(ContextCompat.getColor(context, R.color.purple_700))

    }

    private fun createBoard(canvas: Canvas) {
//        createRectangle(0,0,15,15,canvas,redPaint)
//        createRectangle(0,0,1,1,canvas,yellowPaint)
//
//        createRoundRect(0,0,1,1,canvas,borderPaint)

        createNormalTiles(canvas)
        createRestingPlaces(canvas)
    }

    private fun createRectangle(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        canvas: Canvas,
        paint: Paint
    ) {
        val rectF = RectF(column[x], row[y], column[width], row[height])
        canvas.drawRect(rectF, paint)
    }

    private fun createRoundRect(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        canvas: Canvas,
        startColor: Int,
        endColor: Int,
        radius: Float
    ) {
        val mShader: Shader = LinearGradient(
            column[x] + cellPadding,
            row[y] + cellPadding,
            column[width] - cellPadding,
            row[height] - cellPadding,
            ContextCompat.getColor(context, startColor), ContextCompat.getColor(context, endColor),
            TileMode.CLAMP
        )

        val paint = Paint()
        paint.apply {
            shader = mShader
        }

        val rectF = RectF(
            column[x] + cellPadding,
            row[y] + cellPadding,
            column[x + width] - cellPadding,
            row[y + height] - cellPadding
        )
        canvas.drawRoundRect(rectF, radius, radius, paint)
        canvas.drawRoundRect(rectF, radius, radius, borderPaint)
    }

    private fun drawDrawables(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        canvas: Canvas,
        img: Int,
        padding: Int
    ) {
        val mDrawable = ContextCompat.getDrawable(context, img)
        val dPadding = cellPadding * padding
        mDrawable!!.setBounds(
            (column[x] + dPadding).toInt(),
            (row[y] + dPadding).toInt(),
            (column[x + width] - dPadding).toInt(),
            (row[y + height] - dPadding).toInt()
        )

        mDrawable.draw(canvas)
    }

    private fun createNormalTiles(canvas: Canvas) {
        val greyStar = R.drawable.ic_grey_star
        val whiteStar = R.drawable.ic_white_star

        // left side
        for (i in 6..8) {
            for (j in 0..5) {
                if (i == 6 && j == 1) {
                    createRoundRect(
                        j, i, 1, 1,
                        canvas,
                        R.color.start_green,
                        R.color.end_green,
                        tileRadius
                    )
                    drawDrawables(
                        j, i, 1, 1,
                        canvas, whiteStar, 3
                    )

                } else if (i == 7 && j > 0) {
                    createRoundRect(
                        j, i, 1, 1,
                        canvas,
                        R.color.start_green,
                        R.color.end_green,
                        tileRadius
                    )
                } else if (i == 8 && j == 2) {
                    createRoundRect(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        R.color.start_white,
                        R.color.end_white,
                        tileRadius
                    )
                    drawDrawables(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        greyStar, 3
                    )
                } else {
                    createRoundRect(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        R.color.start_white,
                        R.color.end_white,
                        tileRadius
                    )
                }
            }
        }

        // top side
        for (i in 0..5) {
            for (j in 6..8) {
                if (i == 1 && j == 8) {
                    createRoundRect(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        R.color.start_red,
                        R.color.end_red,
                        tileRadius
                    )

                    drawDrawables(
                        j, i, 1, 1,
                        canvas, whiteStar, 3
                    )
                } else if (j == 7 && i > 0) {
                    createRoundRect(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        R.color.start_red,
                        R.color.end_red,
                        tileRadius
                    )
                } else if (j == 6 && i == 2) {
                    createRoundRect(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        R.color.start_white,
                        R.color.end_white,
                        tileRadius
                    )
                    drawDrawables(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        greyStar, 3
                    )
                } else {
                    createRoundRect(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        R.color.start_white,
                        R.color.end_white,
                        tileRadius
                    )
                }

//                createRoundRect(j,i,1,1,canvas,R.color.start_white,R.color.end_white,tileRadius)
            }
        }

        // right side
        for (i in 6..8) {
            for (j in 9..14) {
                if (i == 8 && j == 13) {
                    createRoundRect(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        R.color.start_blue,
                        R.color.end_blue,
                        tileRadius
                    )
                    drawDrawables(
                        j, i, 1, 1,
                        canvas, whiteStar, 3
                    )
                } else if (i == 7 && j < 14) {
                    createRoundRect(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        R.color.start_blue,
                        R.color.end_blue,
                        tileRadius
                    )
                } else if (i == 6 && j == 12) {
                    createRoundRect(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        R.color.start_white,
                        R.color.end_white,
                        tileRadius
                    )
                    drawDrawables(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        greyStar, 3
                    )
                } else {
                    createRoundRect(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        R.color.start_white,
                        R.color.end_white,
                        tileRadius
                    )
                }
            }
        }

        // bottom side
        for (i in 9..14) {
            for (j in 6..8) {
                if (i == 13 && j == 6) {
                    createRoundRect(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        R.color.start_yellow,
                        R.color.end_yellow,
                        tileRadius
                    )
                    drawDrawables(
                        j, i, 1, 1,
                        canvas, whiteStar, 3
                    )
                } else if (j == 7 && i < 14) {
                    createRoundRect(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        R.color.start_yellow,
                        R.color.end_yellow,
                        tileRadius
                    )
                } else if (j == 8 && i == 12) {
                    createRoundRect(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        R.color.start_white,
                        R.color.end_white,
                        tileRadius
                    )
                    drawDrawables(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        greyStar, 3
                    )
                } else {
                    createRoundRect(
                        j,
                        i,
                        1,
                        1,
                        canvas,
                        R.color.start_white,
                        R.color.end_white,
                        tileRadius
                    )
                }
            }
        }
        drawDrawables(6, 6, 3, 3, canvas, R.drawable.ic_ludo_center, 1)
    }

    private fun createRestingPlaces(canvas: Canvas) {
        createRoundRect(
            0, 0, 6, 6, canvas, R.color.start_green, R.color.end_green, restingPlaceRadius
        )

        createCircle(3,3,canvas,whitePaint,d*2)

        createRoundRect(
            9, 0, 6, 6, canvas, R.color.start_red, R.color.end_red, restingPlaceRadius
        )
        createCircle(12,3,canvas,whitePaint,d*2)
        createRoundRect(
            0, 9, 6, 6, canvas, R.color.start_yellow, R.color.end_yellow, restingPlaceRadius
        )

        createCircle(3,12,canvas,whitePaint,d*2)
        createRoundRect(
            9, 9, 6, 6, canvas, R.color.start_blue, R.color.end_blue, restingPlaceRadius
        )
        createCircle(12,12,canvas,whitePaint,d*2)

    }

    private fun createPlayersSpot(canvas: Canvas){
        createSpots(2,2,canvas,greenPaint,d/3)
        createSpots(4,2,canvas,greenPaint,d/3)
        createSpots(2,4,canvas,greenPaint,d/3)
        createSpots(4,4,canvas,greenPaint,d/3)

    }

    private fun createCircle(
        x: Int,
        y: Int,
        canvas: Canvas,
        paint: Paint,
        radius: Float
    ) {
        canvas.drawCircle(column[x],row[y],radius, paint)
    }
    private fun createSpots(
        x: Int,
        y: Int,
        canvas: Canvas,
        paint: Paint,
        radius: Float
    ) {
        canvas.drawCircle(column[x]+d/2,row[y]+d/2,radius, paint)
    }


    private fun populateDisplayMatrix() {
        for (i in 0..15) column.add(d * i)
        for (j in 0..15) row.add(topSpacing + d * j)

//        for (i in 0..14){
//            val row = mutableListOf<Pair<Float,Float>>()
//            for (j in 0..14){
//                row.add(Pair(d*j,topSpacing+d*i))
//            }
//            _matrix.add(row)
//        }
    }

}