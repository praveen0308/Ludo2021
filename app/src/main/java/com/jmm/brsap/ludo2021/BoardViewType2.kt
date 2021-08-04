package com.jmm.brsap.ludo2021

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class BoardViewType2(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private lateinit var borderPaint:Paint
    private lateinit var redPaint:Paint
    private lateinit var greenPaint:Paint
    private lateinit var yellowPaint:Paint
    private lateinit var bluePaint:Paint


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        initializePainters()
        canvas.drawColor(Color.BLACK)
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


}