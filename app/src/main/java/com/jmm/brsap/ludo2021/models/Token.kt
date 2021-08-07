package com.jmm.brsap.ludo2021.models

data class Token(
    val id : Int,
    val token_no:Int,
    var colors: PlayerColors,
    var standingAt : Int,
    var isFree : Boolean = false,
    var isWon : Boolean = false,
    val drawingObject: DrawingObject
)
