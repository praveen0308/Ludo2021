package com.jmm.brsap.ludo2021.models

import android.widget.ImageView

data class Dice(
    val color:PlayerColors,
    var diceState:DiceState,
    val diceView : ImageView
)
