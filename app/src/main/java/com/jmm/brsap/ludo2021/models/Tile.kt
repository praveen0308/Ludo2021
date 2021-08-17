package com.jmm.brsap.ludo2021.models

data class Tile(
    var id: Int,
    var column:Int,
    var row:Int,
    val isStop: Boolean = false,
    val color: PlayerColors = PlayerColors.WHITE,
    val gateFor: PlayerColors = PlayerColors.WHITE,
    var tokens : MutableList<Token> = mutableListOf(),
)