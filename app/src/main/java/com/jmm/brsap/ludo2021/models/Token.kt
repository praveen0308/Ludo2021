package com.jmm.brsap.ludo2021.models

import android.widget.ImageView
import com.jmm.brsap.ludo2021.TokenView

data class Token(
    val tokenNo:Int,
    var color: PlayerColors,
    var playerNo:Int,
    var startingFrom:Int,
    var tokenImage:TokenView,
    var standingAt : Int=-1,
    var isFree : Boolean = false,
    var isWon : Boolean = false,
    var canMove : Boolean = false,
    var stepsCompleted : Int = 0
)
