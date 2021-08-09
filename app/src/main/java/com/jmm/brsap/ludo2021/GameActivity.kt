package com.jmm.brsap.ludo2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
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


    private var column = mutableListOf<Float>()
    private var row= mutableListOf<Float>()

    private val boardTiles = mutableListOf<Tile>()
    private val homePaths = mutableListOf<MutableList<Tile>>()
    private val restingPlaces = mutableListOf<RestingPlace>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareDimensions()
        prepareLudoMap()

        binding.boardView.setLudoMap(ludoMap)
        placeTokens()
    }

    private fun placeTokens(){
        placeView(binding.ivYellow1,column[boardTiles[0].column-1],row[boardTiles[0].row-1])
        for (place in ludoMap.restingPlaces){
            when(place.color){
                PlayerColors.YELLOW->{
                    for (spot in place.spots){
                        placeView(binding.ivYellow1,spot.pX,spot.pY)
                    }
                }
            }
        }
    }

    private fun placeView(view:View,x:Float,y:Float){
        view.layoutParams.height = (d/2).toInt()
        view.layoutParams.width = (d/2).toInt()

        val layoutParams =view.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.marginStart = x.toInt()
        layoutParams.topMargin = y.toInt()
        view.layoutParams = layoutParams
    }

    private fun prepareLudoMap() {
        generateRestingPlaces()
        generateTiles()
        generateHomePaths()
        ludoMap = LudoMap(100,PlayersCount.FOUR_PLAYER, emptyList(),restingPlaces,boardTiles,homePaths)
    }

    private fun prepareDimensions(){
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        mHeight = resources.displayMetrics.heightPixels - resources.getDimensionPixelSize(resourceId)
        mWidth = resources.displayMetrics.widthPixels

        topSpacing = (mHeight-mWidth)/2
        bottomSpacing = (mHeight+mWidth)/2
        d = (mWidth / 15).toFloat()

        Log.i("testing","activity height : $mHeight")
        Log.i("testing","activity topSpacing : $topSpacing")
        Log.i("testing","activity d : $d")

        for (i in 0..15) column.add(d * i)
        for (j in 0..15) row.add(topSpacing + d * j)
    }

    private fun generateHomePaths(){
        val yPaths = mutableListOf<Tile>()
        for (i in 14 downTo 10){
            yPaths.add(Tile(1,8,i,false,PlayerColors.YELLOW))
        }

        val gPaths = mutableListOf<Tile>()
        for (i in 2..6){
            gPaths.add(Tile(2,i,8,false,PlayerColors.GREEN))
        }

        val rPaths = mutableListOf<Tile>()
        for (i in 2..6){
            rPaths.add(Tile(3,8,i,false,PlayerColors.RED))
        }

        val bPaths = mutableListOf<Tile>()
        for (i in 14 downTo 10){
            bPaths.add(Tile(4,i,8,false,PlayerColors.BLUE))
        }

        homePaths.add(yPaths)
        homePaths.add(gPaths)
        homePaths.add(rPaths)
        homePaths.add(bPaths)

    }

    private fun generateTiles() {
        var tile:Tile
        for (i in 0..51){
            when(i){
                0->{
                    tile = Tile(i,7,14,true,PlayerColors.YELLOW)
                }
                1->{
                    tile = Tile(i,7,13)
                }
                2->{
                    tile = Tile(i,7,12)
                }
                3->{
                    tile = Tile(i,7,11)
                }
                4->{
                    tile = Tile(i,7,10)
                }
                5->{
                    tile = Tile(i,6,9)
                }
                6->{
                    tile = Tile(i,5,9)
                }
                7->{
                    tile = Tile(i,4,9)
                }
                8->{
                    tile = Tile(i,3,9,true)
                }
                9->{
                    tile = Tile(i,2,9)
                }
                10->{
                    tile = Tile(i,1,9)
                }
                11->{
                    tile = Tile(i,1,8,false,PlayerColors.WHITE,PlayerColors.GREEN)
                }
                12->{
                    tile = Tile(i,1,7)
                }
                13->{
                    tile = Tile(i,2,7,true,PlayerColors.GREEN)
                }
                14->{
                    tile = Tile(i,3,7)
                }
                15->{
                    tile = Tile(i,4,7)
                }
                16->{
                    tile = Tile(i,5,7)
                }
                17->{
                    tile = Tile(i,6,7)
                }
                18->{
                    tile = Tile(i,7,6)
                }
                19->{
                    tile = Tile(i,7,5)
                }
                20->{
                    tile = Tile(i,7,4)
                }
                21->{
                    tile = Tile(i,7,3,true)
                }
                22->{
                    tile = Tile(i,7,2)
                }
                23->{
                    tile = Tile(i,7,1)
                }
                24->{
                    tile = Tile(i,8,1,false,PlayerColors.WHITE,PlayerColors.RED)
                }
                25->{
                    tile = Tile(i,9,1)
                }
                26->{
                    tile = Tile(i,9,2,true,PlayerColors.RED)
                }
                27->{
                    tile = Tile(i,9,3)
                }
                28->{
                    tile = Tile(i,9,4)
                }
                29->{
                    tile = Tile(i,9,5)
                }
                30->{
                    tile = Tile(i,9,6)
                }
                31->{
                    tile = Tile(i,10,7)
                }
                32->{
                    tile = Tile(i,11,7)
                }
                33->{
                    tile = Tile(i,12,7)
                }
                34->{
                    tile = Tile(i,13,7,true)
                }
                35->{
                    tile = Tile(i,14,7)
                }
                36->{
                    tile = Tile(i,15,7)
                }
                37->{
                    tile = Tile(i,15,8,false,PlayerColors.WHITE,PlayerColors.BLUE)
                }
                38->{
                    tile = Tile(i,15,9)
                }
                39->{
                    tile = Tile(i,14,9,true,PlayerColors.BLUE)
                }
                40->{
                    tile = Tile(i,13,9)
                }
                41->{
                    tile = Tile(i,12,9)
                }
                42->{
                    tile = Tile(i,11,9)
                }
                43->{
                    tile = Tile(i,10,9)
                }
                44->{
                    tile = Tile(i,9,10)
                }
                45->{
                    tile = Tile(i,9,11)
                }
                46->{
                    tile = Tile(i,9,12)
                }
                47->{
                    tile = Tile(i,9,13,true)
                }
                48->{
                    tile = Tile(i,9,14)
                }
                49->{
                    tile = Tile(i,9,15)
                }
                50->{
                    tile = Tile(i,8,15,false,PlayerColors.WHITE,PlayerColors.YELLOW)
                }
                51->{
                    tile = Tile(i,7,15)
                }
                else->{
                    tile = Tile(i,i,i)
                }
            }

            boardTiles.add(tile)
        }

    }

    private fun generateRestingPlaces(){
        var x = 2
        var y=11

        // preparing green token spots
        val ySpots = mutableListOf<Spot>()

        ySpots.add(Spot(1,column[x]+d/4,row[y]+d/3,d/3,PlayerColors.YELLOW))
        ySpots.add(Spot(2,column[x+2]-d/4,row[y]+d/3,d/3,PlayerColors.YELLOW))
        ySpots.add(Spot(3,column[x]+d/4,row[y+2]-d/3,d/3,PlayerColors.YELLOW))
        ySpots.add(Spot(4,column[x+2]-d/4,row[y+2]-d/3,d/3,PlayerColors.YELLOW))

        val ySpotBase = SpotBase(1,3,12,d*2)
        val yRestingPlace = RestingPlace(PlayerColors.YELLOW,ySpotBase,0,9,ySpots)

        x=2
        y=2

        // preparing green token spots
        val gSpots = mutableListOf<Spot>()

        gSpots.add(Spot(1,column[x]+d/4,row[y]+d/3,d/3,PlayerColors.GREEN))
        gSpots.add(Spot(2,column[x+2]-d/4,row[y]+d/3,d/3,PlayerColors.GREEN))
        gSpots.add(Spot(3,column[x]+d/4,row[y+2]-d/3,d/3,PlayerColors.GREEN))
        gSpots.add(Spot(4,column[x+2]-d/4,row[y+2]-d/3,d/3,PlayerColors.GREEN))

        val gSpotBase = SpotBase(2,3,3,d*2)
        val gRestingPlace = RestingPlace(PlayerColors.GREEN,gSpotBase,0,0,gSpots)

        x=11
        y=2

        // preparing green token spots
        val rSpots = mutableListOf<Spot>()

        rSpots.add(Spot(1,column[x]+d/4,row[y]+d/3,d/3,PlayerColors.RED))
        rSpots.add(Spot(2,column[x+2]-d/4,row[y]+d/3,d/3,PlayerColors.RED))
        rSpots.add(Spot(3,column[x]+d/4,row[y+2]-d/3,d/3,PlayerColors.RED))
        rSpots.add(Spot(4,column[x+2]-d/4,row[y+2]-d/3,d/3,PlayerColors.RED))

        val rSpotBase = SpotBase(3,12,3,d*2)
        val rRestingPlace = RestingPlace(PlayerColors.RED,rSpotBase,9,0,rSpots)


        x=11
        y=11

        // preparing green token spots
        val bSpots = mutableListOf<Spot>()
        bSpots.add(Spot(1,column[x]+d/4,row[y]+d/3,d/3,PlayerColors.BLUE))
        bSpots.add(Spot(2,column[x+2]-d/4,row[y]+d/3,d/3,PlayerColors.BLUE))
        bSpots.add(Spot(3,column[x]+d/4,row[y+2]-d/3,d/3,PlayerColors.BLUE))
        bSpots.add(Spot(4,column[x+2]-d/4,row[y+2]-d/3,d/3,PlayerColors.BLUE))

        val bSpotBase = SpotBase(4,12,12,d*2)
        val bRestingPlace = RestingPlace(PlayerColors.BLUE,bSpotBase,9,9,bSpots)

        restingPlaces.add(yRestingPlace)
        restingPlaces.add(gRestingPlace)
        restingPlaces.add(rRestingPlace)
        restingPlaces.add(bRestingPlace)
    }


}