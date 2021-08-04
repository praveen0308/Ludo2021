package com.jmm.brsap.ludo2021

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class BoardViewType2(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    // Painter
    private lateinit var borderPaint:Paint
    private lateinit var redPaint:Paint
    private lateinit var greenPaint:Paint
    private lateinit var yellowPaint:Paint
    private lateinit var bluePaint:Paint

    // dimension of single cell into ludo map
    private var d: Float = 0f

    // screen width and height
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    // top and bottom padding to create 15x15 square map
    private var topSpacing : Int = 0
    private var bottomSpacing : Int = 0

    // cell padding
    private var cellPadding : Float = 0f

    // room padding
    private var restingRoomPadding : Float = 0f

    // screen matrix
    private val _matrix = mutableListOf<MutableList<Pair<Float,Float>>>()
    private val column = mutableListOf<Float>()
    private val row = mutableListOf<Float>()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLACK)
        prepareScreenDimensions()
        populateDisplayMatrix()
        initializePainters()
        createBoard(canvas)

    }

    private fun prepareScreenDimensions(){
        mWidth = width
        mHeight = height

        d = (width / 15).toFloat()
        /*
        * This for making map 15x15 square shaped
        * */
        topSpacing = (mHeight - mWidth) / 2
        bottomSpacing = (mHeight + mWidth) / 2

        cellPadding = d/12

        restingRoomPadding = d/8
    }

    private fun initializePainters(){
        borderPaint= Paint()

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
            style = Paint.Style.FILL
        }

        bluePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bluePaint.apply {
            color = Color.BLUE
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        yellowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        yellowPaint.apply {
            color = Color.YELLOW
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        greenPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        greenPaint.apply {
            color = Color.GREEN
            isAntiAlias = true
            style = Paint.Style.FILL
        }

    }

    private fun createBoard(canvas: Canvas){

    }


    private fun createRectangle(
        x:Int,
        y:Int,
        width:Int,
        height:Int,
        canvas: Canvas,
        paint:Paint
    ){
        val rectF = RectF(column[x],row[y],column[width],row[height])
        canvas.drawRect(rectF,redPaint)
    }

    private fun populateDisplayMatrix(){
        for (i in 0..14) column.add(d*i)
        for (j in 0..14) row.add(topSpacing+d*j)

//        for (i in 0..14){
//            val row = mutableListOf<Pair<Float,Float>>()
//            for (j in 0..14){
//                row.add(Pair(d*j,topSpacing+d*i))
//            }
//            _matrix.add(row)
//        }
    }
}