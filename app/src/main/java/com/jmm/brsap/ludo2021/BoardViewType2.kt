package com.jmm.brsap.ludo2021

import android.content.Context
import android.graphics.*
import android.graphics.Shader.TileMode
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat


class BoardViewType2(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    // Painter
    private lateinit var borderPaint:Paint
    private lateinit var redPaint:Paint
    private lateinit var whitePaint:Paint
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
            style = Paint.Style.FILL
        }

        val mShader: Shader = LinearGradient(0f, topSpacing.toFloat(), d, topSpacing+d, Color.BLUE, Color.GREEN, TileMode.CLAMP)
        yellowPaint = Paint()
        yellowPaint.apply {
            shader = mShader
        }

        greenPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        greenPaint.apply {
            color = Color.GREEN
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        greenPaint.setColor(ContextCompat.getColor(context,R.color.purple_700))

    }

    private fun createBoard(canvas: Canvas){
        createRectangle(0,0,15,15,canvas,whitePaint)
        createRectangle(0,0,1,1,canvas,yellowPaint)
//        createRoundRect(0,0,1,1,canvas,borderPaint)
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
        canvas.drawRect(rectF,paint)
    }

    private fun createRoundRect(
        x:Int,
        y:Int,
        width:Int,
        height:Int,
        canvas: Canvas,
        paint:Paint
    ){
        val rectF = RectF(column[x]+cellPadding,row[y]+cellPadding,column[width]-cellPadding,row[height]-cellPadding)
        canvas.drawRoundRect(rectF,d/10,d/10,paint)
    }
    private fun populateDisplayMatrix(){
        for (i in 0..15) column.add(d*i)
        for (j in 0..15) row.add(topSpacing+d*j)

//        for (i in 0..14){
//            val row = mutableListOf<Pair<Float,Float>>()
//            for (j in 0..14){
//                row.add(Pair(d*j,topSpacing+d*i))
//            }
//            _matrix.add(row)
//        }
    }
}