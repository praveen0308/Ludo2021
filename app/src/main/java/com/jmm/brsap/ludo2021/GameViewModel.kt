package com.jmm.brsap.ludo2021

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jmm.brsap.ludo2021.models.PlayerColors

class GameViewModel : ViewModel(){

    val activePlayer = MutableLiveData<PlayerColors>(PlayerColors.YELLOW)
}