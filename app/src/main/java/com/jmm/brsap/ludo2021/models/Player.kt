package com.jmm.brsap.ludo2021.models

data class Player(
    val id : Int,
    val name:String,
    val color:PlayerColors,
    val number : PlayerNumbers,
    var isActive : Boolean=false,
    var isWon : Boolean = false,
    var isQuit : Boolean = false,
    var tokens : List<Token>
)
