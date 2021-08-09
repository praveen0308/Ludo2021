package com.jmm.brsap.ludo2021.models

import android.graphics.Paint

data class DrawingObject(
    var pX1:Int,
    var pY1:Int,
    var pX2:Int,
    var pY2:Int,
    var paint: Paint,
    var radius: Float,
    var padding:Float,
    var isStroke:Boolean = true
)
