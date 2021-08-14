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
    /*
    *
    * READY >>> dice is visible and clickable
    *
    * SPINNING >>> dice animation is visible and non clickable
    *
    * WAITING >>> dice is visible but not clickable, waiting for player to move token
    *
    * IDLE >>> dice is visible and not clickable , soon will be disappeared then will rest
    *
    * REST >>> dice is invisible and not clickable, when other players playing chance
    *
    * */



    READY,      // player had already moved a token or ready to play
    SPINNING,   // dice is spinning
    WAITING,    // dice is waiting for player to move a token
    IDLE,       // dice is steady with no action
    REST,        // dice will be invisible and non clickable


}