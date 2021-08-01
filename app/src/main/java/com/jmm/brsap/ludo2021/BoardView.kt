package com.jmm.brsap.ludo2021

import android.R.attr
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat


class BoardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private lateinit var paint: Paint
    private lateinit var borderPaint: Paint
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
        initializePainter()
        createPlayerAreas(canvas)
        generatePaths(canvas)
        drawCentralHomeView(canvas)
        generateSafePoints(canvas)

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


    }

    private fun initializePainter(){

        // Initializing paint object
        paint= Paint()
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

        val x = d.toInt()
        val pointA = Point(x*6,topSpacing+x*6)
        val pointB = Point(x*9,topSpacing+x*6)
        val pointC = Point(x*6,topSpacing+x*9)
        val pointD = Point(x*9,topSpacing+x*9)
        val pointE = Point((x*7.5).toInt(),(topSpacing+x*7.5).toInt())

        drawTriangle(pointA,pointB,pointE,redPaint,canvas)
        drawTriangle(pointA,pointC,pointE,greenPaint,canvas)
        drawTriangle(pointC,pointD,pointE,yellowPaint,canvas)
        drawTriangle(pointD,pointB,pointE,bluePaint,canvas)

    }

    private fun drawTriangle(
        point1:Point,
        point2:Point,
        point3:Point,
        mPaint: Paint,
        canvas: Canvas
    ) {
        val path = Path()
        path.fillType = Path.FillType.EVEN_ODD
        path.moveTo(point1.x.toFloat(), point1.y.toFloat())
        path.lineTo(point2.x.toFloat(), point2.y.toFloat())
        path.lineTo(point3.x.toFloat(), point3.y.toFloat())
        path.close()
        canvas.drawPath(path, mPaint)


        val outLines = Path()
        outLines.fillType = Path.FillType.EVEN_ODD
        outLines.moveTo(point1.x.toFloat(), point1.y.toFloat())
        outLines.lineTo(point2.x.toFloat(), point2.y.toFloat())
        outLines.lineTo(point3.x.toFloat(), point3.y.toFloat())
        outLines.close()
        canvas.drawPath(outLines, borderPaint)
    }

    private fun generateSafePoints(canvas: Canvas){
        val star = ContextCompat.getDrawable(context, R.drawable.ic_round_star_24)
        star?.let {
            val x = d.toInt()

            //green side
            canvas.drawRect(Rect(x, topSpacing+x*6, 2*x, topSpacing+x*7),greenPaint)
            canvas.drawRect(Rect(x, topSpacing+x*6, 2*x, topSpacing+x*7),borderPaint)
            it.setBounds(x, topSpacing+x*6, 2*x, topSpacing+x*7)
            it.draw(canvas)

            it.setBounds(x*2, topSpacing+x*8, 3*x, topSpacing+x*9)
            it.draw(canvas)


            //red side
            it.setBounds(6*x, topSpacing+x*2, 7*x, topSpacing+x*3)
            it.draw(canvas)

            canvas.drawRect(Rect(8*x, topSpacing+x, 9*x, topSpacing+x*2),redPaint)
            canvas.drawRect(Rect(8*x, topSpacing+x, 9*x, topSpacing+x*2),borderPaint)
            it.setBounds(8*x, topSpacing+x, 9*x, topSpacing+x*2)
            it.draw(canvas)


            // blue side
            it.setBounds(x*12, topSpacing+x*6, 13*x, topSpacing+x*7)
            it.draw(canvas)

            canvas.drawRect(Rect(x*13, topSpacing+x*8, 14*x, topSpacing+x*9),bluePaint)
            canvas.drawRect(Rect(x*13, topSpacing+x*8, 14*x, topSpacing+x*9),borderPaint)
            it.setBounds(x*13, topSpacing+x*8, 14*x, topSpacing+x*9)
            it.draw(canvas)

            // yellow side
            canvas.drawRect(Rect(x*6, topSpacing+x*13, 7*x, topSpacing+x*14),yellowPaint)
            canvas.drawRect(Rect(x*6, topSpacing+x*13, 7*x, topSpacing+x*14),borderPaint)
            it.setBounds(x*6, topSpacing+x*13, 7*x, topSpacing+x*14)
            it.draw(canvas)

            it.setBounds(x*8, topSpacing+x*12, 9*x, topSpacing+x*13)
            it.draw(canvas)

        }

    }
}