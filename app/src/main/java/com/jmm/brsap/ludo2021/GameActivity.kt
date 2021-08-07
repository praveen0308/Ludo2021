package com.jmm.brsap.ludo2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jmm.brsap.ludo2021.databinding.ActivityGameBinding
import com.jmm.brsap.ludo2021.models.LudoMap

class GameActivity : AppCompatActivity() {

    private lateinit var ludoMap: LudoMap
    private lateinit var binding: ActivityGameBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareLudoMap()
    }

    private fun prepareDimensions(){
        binding.boardView.getDSize()
    }

    private fun prepareLudoMap() {

    }

}