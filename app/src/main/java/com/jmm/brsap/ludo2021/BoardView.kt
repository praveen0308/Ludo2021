package com.jmm.brsap.ludo2021

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class BoardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private lateinit var paint: Paint
    private lateinit var pathPaint: Paint
    private lateinit var yellowPaint: Paint
    private lateinit var redPaint: Paint
    private lateinit var greenPaint: Paint
    private lateinit var bluePaint: Paint


    // dimension of single cell into ludo map
    private var d: Float = 0f

    // screen width and height
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    // top and bottom padding to create 15x15 square map
    private var topSpacing : Int = 0
    private var bottomSpacing : Int = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        prepareScreenDimensions()
        createPlayerAreas(canvas)
        generatePaths(canvas)
        drawCentralHomeView(canvas)

    }

    private fun prepareScreenDimensions(){
        mWidth = width
        mHeight = height

        d = (width / 15).toFloat()

        /*
        * This for making map 15x15 square shaped
        *
        * */
        topSpacing = (mHeight - mWidth) / 2
        bottomSpacing = (mWidth + mWidth) / 2

        // Initializing paint object
        paint = Paint()
    }

    private fun createPlayerAreas(canvas: Canvas?) {

        // Making green player area
        paint.apply {
            color = Color.GREEN
            isAntiAlias = false
        }
        canvas!!.drawRect(
            0f, topSpacing*1f, d*6, topSpacing+d*6, paint
        )

        // Making red player area
        paint.apply {
            color = Color.RED
            isAntiAlias = false
        }
        canvas.drawRect(
            d*9, topSpacing*1f, d*15, topSpacing+d*6, paint
        )

        // Making blue player area
        paint.apply {
            color = Color.BLUE
            isAntiAlias = false
        }
        canvas.drawRect(
            d*9, topSpacing+d*9, d*15, topSpacing+d*15, paint
        )

        // Making yellow player area
        paint.apply {
            color = Color.YELLOW
            isAntiAlias = false
        }
        canvas.drawRect(
            0f, topSpacing+d*9, d*6, topSpacing+d*15, paint
        )


    }

    private fun generatePaths(canvas: Canvas){

        // Making left side paths
        paint.apply {
            color = Color.GREEN
            isAntiAlias = false
            style = Paint.Style.FILL
        }
        for (j in 1..5){
            canvas.drawRect(j*d, topSpacing+6*d+1*d, j*d+d, topSpacing+d*7+1*d, paint)
        }

        paint.apply {
            color = Color.BLACK
            strokeWidth = 2f
            style = Paint.Style.STROKE
            isAntiAlias = false
        }

        for (i in 0..2){
            for (j in 0..5){
                canvas.drawRect(j*d, topSpacing+6*d+i*d, j*d+d, topSpacing+d*7+i*d, paint)
            }
        }

        // Making right side paths
        paint.apply {
            color = Color.BLUE
            isAntiAlias = false
            style = Paint.Style.FILL
        }
        for (j in 0..4){
            canvas.drawRect(d*9+j*d, topSpacing+6*d+1*d, d*9+j*d+d, topSpacing+d*7+1*d, paint)
        }

        paint.apply {
            color = Color.BLACK
            strokeWidth = 2f
            style = Paint.Style.STROKE
            isAntiAlias = false
        }

        for (i in 0..2){
            for (j in 0..5){
                canvas.drawRect(d*9+j*d, topSpacing+6*d+i*d, d*9+j*d+d, topSpacing+d*7+i*d, paint)
            }
        }


        // Making top side paths
        paint.apply {
            color = Color.RED
            isAntiAlias = false
            style = Paint.Style.FILL
        }


        for (j in 1..5){
            canvas.drawRect(d*6+d, topSpacing+j*d, d*6+1*d+d, topSpacing+j*d+d, paint)
        }
        paint.apply {
            color = Color.BLACK
            strokeWidth = 2f
            style = Paint.Style.STROKE
            isAntiAlias = false
        }

        for (i in 0..2){
            for (j in 0..5){
                canvas.drawRect(d*6+i*d, topSpacing+j*d, d*6+i*d+d, topSpacing+j*d+d, paint)
            }
        }


        // Making bottom side paths
        paint.apply {
            color = Color.YELLOW
            isAntiAlias = false
            style = Paint.Style.FILL
        }


        for (j in 0..4){
            canvas.drawRect(d*6+d, topSpacing+d*9+j*d, d*6+1*d+d, topSpacing+d*9+j*d+d, paint)
        }
        paint.apply {
            color = Color.BLACK
            strokeWidth = 2f
            style = Paint.Style.STROKE
            isAntiAlias = false
        }

        for (i in 0..2){
            for (j in 0..5){
                canvas.drawRect(d*6+i*d, topSpacing+d*9+j*d, d*6+i*d+d, topSpacing+d*9+j*d+d, paint)
            }
        }

    }

    private fun drawCentralHomeView(canvas: Canvas){
        redPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        redPaint.apply {
            color = Color.RED
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        drawTriangle((d*6).toInt(),(topSpacing+d*6).toInt(),(d*3).toInt(),(d*1.5).toInt(),true,redPaint,canvas)
//        val path = Path()
//        path.fillType = Path.FillType.EVEN_ODD
//        path.moveTo(d*6, topSpacing+d*6)
//        path.lineTo(d*9, topSpacing+d*9)
//        path.lineTo(d*7, topSpacing+d*7)
//        path.lineTo(d*6, topSpacing+d*6)
//        path.close()
//
//        canvas.drawPath(path, redPaint)

        val pointA = Point()
        val pointB = Point()
        val pointC = Point()
        val pointD = Point()
        val pointE = Point()

    }

    private fun drawTriangle(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        inverted: Boolean,
        paint: Paint,
        canvas: Canvas
    ) {
        val p1 = Point(x, y)
        val pointX = x + width / 2
        val pointY = if (inverted) y + height else y - height
        val p2 = Point(pointX, pointY)
        val p3 = Point(x + width, y)
        val path = Path()
        path.fillType = Path.FillType.EVEN_ODD
        path.moveTo(p1.x.toFloat(), p1.y.toFloat())
        path.lineTo(p2.x.toFloat(), p2.y.toFloat())
        path.lineTo(p3.x.toFloat(), p3.y.toFloat())
        path.close()
        canvas.drawPath(path, paint)
    }
}