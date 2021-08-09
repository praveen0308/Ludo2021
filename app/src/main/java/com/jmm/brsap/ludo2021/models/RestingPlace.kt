package com.jmm.brsap.ludo2021.models

data class RestingPlace(
    val color: PlayerColors,
    var drawingObject: DrawingObject,
    var spots:List<Spot>
)
