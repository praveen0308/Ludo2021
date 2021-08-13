package com.jmm.brsap.ludo2021

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jmm.brsap.ludo2021.models.Player
import com.jmm.brsap.ludo2021.models.PlayerColors
import kotlinx.coroutines.flow.MutableStateFlow

class GameViewModel : ViewModel(){

    val activeColor = MutableLiveData(PlayerColors.YELLOW)
    val activePlayer = MutableLiveData<Player>()


}