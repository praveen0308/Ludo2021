package com.jmm.brsap.ludo2021.models

import android.widget.ImageView

data class Token(
    val tokenNo:Int,
    var color: PlayerColors,
    var startingFrom:Int,
    var tokenImage:ImageView,
    var standingAt : Int=-1,
    var isFree : Boolean = false,
    var isWon : Boolean = false,
)
