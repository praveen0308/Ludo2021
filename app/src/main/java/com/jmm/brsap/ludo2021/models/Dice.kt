package com.jmm.brsap.ludo2021.models

import android.widget.ImageView

data class Dice(
    val color:PlayerColors,
    var state:DiceState,
    val diceView : ImageView,
    var outCome:Int = 0
)
