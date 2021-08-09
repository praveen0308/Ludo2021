package com.jmm.brsap.ludo2021.models

data class RestingPlace(
    val color: PlayerColors,
    val circularBase:SpotBase,
    var pX: Int,
    var pY:Int,
    var spots:List<Spot>
)
