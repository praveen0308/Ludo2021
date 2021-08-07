package com.jmm.brsap.ludo2021.models

data class LudoMap(
    val id:Int,
    val type : PlayersCount,
    val players:List<Player>,
    val restingPlaces: List<RestingPlace>,
    val tiles : List<Tile>,
    val date: String?=null
)