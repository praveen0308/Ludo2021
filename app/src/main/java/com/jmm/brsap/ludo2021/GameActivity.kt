package com.jmm.brsap.ludo2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jmm.brsap.ludo2021.databinding.ActivityGameBinding
import com.jmm.brsap.ludo2021.models.LudoMap
import com.jmm.brsap.ludo2021.models.Player
import com.jmm.brsap.ludo2021.models.PlayersCount
import java.util.*

class GameActivity : AppCompatActivity() {

    private lateinit var ludoMap: LudoMap
    private lateinit var binding: ActivityGameBinding

    private var players = mutableListOf<Player>()

    // dimension of single cell into ludo map
    private var d: Float = 0f

    // screen width and height
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    // top and bottom padding to create 15x15 square map
    private var topSpacing: Int = 0
    private var bottomSpacing: Int = 0

    // cell padding
    private var cellPadding: Float = 0f

    private lateinit var column : MutableList<Float>
    private lateinit var row : MutableList<Float>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareDimensions()
        prepareLudoMap()
    }

    private fun prepareDimensions(){
        d = binding.boardView.getDSize()
        topSpacing = binding.boardView.getTopSpacing()

        for (i in 0..15) column.add(d * i)
        for (j in 0..15) row.add(topSpacing + d * j)
    }

    private fun prepareLudoMap() {
        // preparing players list



//        ludoMap = LudoMap(
//            1000,
//            PlayersCount.FOUR_PLAYER,
//            players,
//
//        )

    }

    private fun getPlayersList(){
//        players =
    }



}