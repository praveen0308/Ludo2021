package com.jmm.brsap.ludo2021

import android.R.attr
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation


class CanvasTesting(context: Context?, attrs: AttributeSet?) : View(context, attrs){
    private val QUOTE = "Now is the time for all good men to come to the aid of their country."

    private lateinit var mAnimation: Animation
    private lateinit var gPaint: Paint
    private lateinit var cPaint: Paint
    private lateinit var glowCircle: Path
    private lateinit var circle: Path
    private lateinit var tPaint: Paint

    init {
        gPaint = Paint()
        gPaint.alpha = 255
        gPaint.setShadowLayer(40f, 0f, 0f, Color.argb(200, 255, 0, 0))
        cPaint = Paint()
        cPaint.isAntiAlias = true
        cPaint.isDither = true
        val filter = BlurMaskFilter(10f, BlurMaskFilter.Blur.INNER)
        cPaint.maskFilter = filter
        val x = 150
        val y = 150
        val r = 100
        glowCircle = Path()
        glowCircle.addCircle(x.toFloat(), y.toFloat(), r.toFloat(), Path.Direction.CW)
        val color1: Int = Color.rgb(40, 40, 40)
        val color2: Int = Color.rgb(220, 220, 220)
        val gradient: LinearGradient =
            LinearGradient(0f, 0f, 0f, y * 2f, color2, color1, Shader.TileMode.REPEAT)
        cPaint.shader = gradient
        circle = Path()
        circle.addCircle(x.toFloat(), y.toFloat(), r.toFloat(), Path.Direction.CW)
        tPaint = Paint()
        tPaint.textSize = 18f
        tPaint.typeface = Typeface.DEFAULT_BOLD
        tPaint.color = Color.BLACK
        tPaint.isAntiAlias = true
    }
    private fun initAnimation() {
        mAnimation = RotateAnimation(0f, 360f, 150f, 150f)
        mAnimation.setRepeatCount(Animation.INFINITE)
        mAnimation.setRepeatMode(Animation.RESTART)
        mAnimation.setDuration(7500L)
        mAnimation.setInterpolator(LinearInterpolator())
        startAnimation(mAnimation)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (animation == null)
            initAnimation();

        canvas.drawPath(glowCircle, gPaint);
        canvas.drawPath(circle, cPaint);
        canvas.drawTextOnPath(QUOTE, circle, 0f, 20f, tPaint);
    }
}