package com.jmm.brsap.ludo2021.models

import android.graphics.Paint

data class DrawingObject(
    var pX1:Float,
    var pY1:Float,
    var pX2:Float,
    var pY2:Float,
    var paint: Paint,
    var radius: Float,
    var padding:Float,
    var isStroke:Boolean = true
)
