package com.jmm.brsap.ludo2021.models

data class Tile(
    var id: Int,
    val isStop: Boolean = false,
    val color: PlayerColors,
    val isGate: Boolean = false,
    val drawingObject: DrawingObject

)