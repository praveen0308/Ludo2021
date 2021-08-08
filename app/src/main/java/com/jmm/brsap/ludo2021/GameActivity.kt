package com.jmm.brsap.ludo2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jmm.brsap.ludo2021.databinding.ActivityGameBinding
import com.jmm.brsap.ludo2021.models.*

class GameActivity : AppCompatActivity() {

    private lateinit var ludoMap: LudoMap
    private lateinit var binding: ActivityGameBinding

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

    // room padding
    private var restingRoomPadding: Float = 0f

    // tile radius
    private var tileRadius: Float = 0f

    // resting place radius
    private var restingPlaceRadius: Float = 0f


    private lateinit var column : MutableList<Float>
    private lateinit var row : MutableList<Float>

    private lateinit var paths : List<List<Token>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareDimensions()
        prepareLudoMap()
    }

    private fun prepareDimensions(){
        mHeight = resources.displayMetrics.heightPixels
        mWidth = resources.displayMetrics.widthPixels

        topSpacing = (mHeight-mWidth)/2
        bottomSpacing = (mHeight+mWidth)/2

        d = (mWidth / 15).toFloat()
        for (i in 0..15) column.add(d * i)
        for (j in 0..15) row.add(topSpacing + d * j)
    }

    private fun prepareLudoMap() {

    }

    private fun generateRestingPlaces(){
        // preparing green token spots
//        val greenSpot1 = Spot(1, DrawingObject(
//            2,2,3,3
//        ))
//
//
//
    }


}