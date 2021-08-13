package com.jmm.brsap.ludo2021.models

enum class PlayerColors {
    RED,BLUE,YELLOW,GREEN,WHITE
}

enum class PlayerNumbers{
    PLAYER_1,
    PLAYER_2,
    PLAYER_3,
    PLAYER_4,
    PLAYER_5,
    PLAYER_6
}

enum class PlayersCount{
    TWO_PLAYER,
    THREE_PLAYER,
    FOUR_PLAYER,
    SIX_PLAYER,
}

enum class DiceState{
    WAITING,    // dice is waiting for player to move a token
    READY,      // player had already moved a token or ready to play
    SPINNING    // dice is spinning
}