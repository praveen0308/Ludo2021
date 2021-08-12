package com.jmm.brsap.ludo2021

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.jmm.brsap.ludo2021.databinding.ActivityGameBinding
import com.jmm.brsap.ludo2021.models.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity() {

    private val viewModel by viewModels<GameViewModel>()
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

    private var column = mutableListOf<Float>()
    private var row= mutableListOf<Float>()

    private val boardTiles = mutableListOf<Tile>()
    private val homePaths = mutableListOf<MutableList<Tile>>()
    private val restingPlaces = mutableListOf<RestingPlace>()
    private val dices = mutableListOf<Pair<PlayerColors,ImageView>>()
//    private val yellowTokens = mutableListOf<Pair<Int,ImageView>>()
//    private val greenTokens = mutableListOf<Pair<Int,ImageView>>()
//    private val redTokens = mutableListOf<Pair<Int,ImageView>>()
//    private val blueTokens = mutableListOf<Pair<Int,ImageView>>()
    private val colors = hashMapOf<Int,PlayerColors>()

    private val TAG = "GameActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareDimensions()
        prepareLudoMap()

        populateViews()
        subscribeObservers()

        binding.boardView.setLudoMap(ludoMap)
        placeTokens()
        placeDice()

        viewModel.activePlayer.postValue(ludoMap.players[0])  // first active yellow player

        binding.apply {

            ivDice1.setOnClickListener {
                performDiceClick(1)
            }

            ivDice2.setOnClickListener {
                performDiceClick(2)
            }

            ivDice3.setOnClickListener {
                performDiceClick(3)
            }

            ivDice4.setOnClickListener {
                performDiceClick(4)
            }
        }


    }

    private fun subscribeObservers(){
        viewModel.activeColor.observe(this,{color->
            lifecycleScope.launch {
                delay(800)
                dices.onEach {
                    it.second.isVisible = it.first == color
                }

            }
            for (player in ludoMap.players){
                if (player.color==color){
                    viewModel.activePlayer.postValue(player)
                }

            }
        })

        viewModel.activePlayer.observe(this,{ activePlayer->
            for(player in ludoMap.players){
                if (player.id == activePlayer.id){
                    for(token in player.tokens){
                        lifecycleScope.launch {
                            while (true) animateToken(token.color)
                        }

                    }
                }
            }

        })
    }

    private suspend fun animateToken(color:PlayerColors){
        for (player in ludoMap.players){
            if (player.color == color){
                player.tokens.onEach {
                    it.tokenImage.animate().alpha(0.7f).setDuration(500).withEndAction {
                        it.tokenImage.alpha = 1f

                    }
                }
            }
        }

    }
    private fun performDiceClick(index: Int){
        var ivDice = ImageView(this)

        for (dice in dices){
            if (dice.first == colors[index]) ivDice = dice.second
        }

        ivDice.animate().scaleX(1.5f).scaleY(1.5f).rotation(-45f).rotationY(1080f).setDuration(500).withEndAction {
            ivDice.scaleX = 1f
            ivDice.scaleY = 1f
            ivDice.rotation = 0f
            ivDice.rotationY = 0f

            val number = (1..6).random()
            ivDice.setImageResource(getRandomDiceFace(number))
            if (number!=6) viewModel.activeColor.postValue(colors[if (index<4) index+1 else 1])
        }
    }


    private fun getRandomDiceFace(index : Int):Int{
        val diceFaces = HashMap<Int,Int>()
        diceFaces[1] = R.drawable.dice1
        diceFaces[2] = R.drawable.dice2
        diceFaces[3] = R.drawable.dice3
        diceFaces[4] = R.drawable.dice4
        diceFaces[5] = R.drawable.dice5
        diceFaces[6] = R.drawable.dice6

        return diceFaces[index]!!
    }




    private fun populateViews(){

        colors[1] = PlayerColors.YELLOW
        colors[2] = PlayerColors.GREEN
        colors[3] = PlayerColors.RED
        colors[4] = PlayerColors.BLUE

        dices.add(Pair(PlayerColors.YELLOW,binding.ivDice1))
        dices.add(Pair(PlayerColors.GREEN,binding.ivDice2))
        dices.add(Pair(PlayerColors.RED,binding.ivDice3))
        dices.add(Pair(PlayerColors.BLUE,binding.ivDice4))


    }
    private fun prepareLudoMap() {
        generateRestingPlaces()
        generateTiles()
        generateHomePaths()

        val diceSpots = mutableListOf<DiceSpot>()
        diceSpots.add(DiceSpot(d/10,topSpacing+15*d,3*d/2,topSpacing+15*d+3*d/2,PlayerColors.YELLOW,d/5))
        diceSpots.add(DiceSpot(d/10,topSpacing-3*d/2,3*d/2,topSpacing*1f,PlayerColors.GREEN,d/5))
        diceSpots.add(DiceSpot(14*d-d/2,topSpacing-3*d/2,14*d+2*d/2-d/10,topSpacing*1f,PlayerColors.RED,d/5))
        diceSpots.add(DiceSpot(14*d-d/2,topSpacing+15*d,14*d+2*d/2-d/10,topSpacing+15*d+3*d/2,PlayerColors.BLUE,d/5))


        val players = mutableListOf<Player>()
        val yellowTokens = mutableListOf<Token>()
        yellowTokens.add(Token(1,PlayerColors.YELLOW,0,binding.ivYellow1))
        yellowTokens.add(Token(2,PlayerColors.YELLOW,0,binding.ivYellow1))
        yellowTokens.add(Token(3,PlayerColors.YELLOW,0,binding.ivYellow1))
        yellowTokens.add(Token(4,PlayerColors.YELLOW,0,binding.ivYellow1))
        players.add(Player(1,"Player 1",PlayerColors.YELLOW,PlayerNumbers.PLAYER_1,tokens = yellowTokens))

        val greenTokens = mutableListOf<Token>()
        greenTokens.add(Token(1,PlayerColors.GREEN,13,binding.ivGreen1))
        greenTokens.add(Token(2,PlayerColors.GREEN,13,binding.ivGreen2))
        greenTokens.add(Token(3,PlayerColors.GREEN,13,binding.ivGreen3))
        greenTokens.add(Token(4,PlayerColors.GREEN,13,binding.ivGreen4))
        players.add(Player(2,"Player 2",PlayerColors.GREEN,PlayerNumbers.PLAYER_2,tokens = greenTokens))

        val redTokens = mutableListOf<Token>()
        redTokens.add(Token(1,PlayerColors.RED,26,binding.ivRed1))
        redTokens.add(Token(2,PlayerColors.RED,26,binding.ivRed2))
        redTokens.add(Token(3,PlayerColors.RED,26,binding.ivRed3))
        redTokens.add(Token(4,PlayerColors.RED,26,binding.ivRed4))
        players.add(Player(3,"Player 3",PlayerColors.RED,PlayerNumbers.PLAYER_3,tokens = redTokens))

        val blueTokens = mutableListOf<Token>()
        blueTokens.add(Token(1,PlayerColors.BLUE,39,binding.ivBlue1))
        blueTokens.add(Token(2,PlayerColors.BLUE,39,binding.ivBlue2))
        blueTokens.add(Token(3,PlayerColors.BLUE,39,binding.ivBlue3))
        blueTokens.add(Token(4,PlayerColors.BLUE,39,binding.ivBlue4))
        players.add(Player(4,"Player 4",PlayerColors.BLUE,PlayerNumbers.PLAYER_4,tokens = blueTokens))

        ludoMap = LudoMap(100,PlayersCount.FOUR_PLAYER, players,restingPlaces,boardTiles,homePaths,diceSpots)
    }
    private fun placeTokens(){
        for (place in ludoMap.restingPlaces){
            when(place.color){
                PlayerColors.YELLOW->{
                    for (spot in place.spots){
                        when(spot.id){
                            1->{placeTokenOnSpot(binding.ivYellow1,spot.pX,spot.pY) }
                            2->{placeTokenOnSpot(binding.ivYellow2,spot.pX,spot.pY) }
                            3->{placeTokenOnSpot(binding.ivYellow3,spot.pX,spot.pY) }
                            4->{placeTokenOnSpot(binding.ivYellow4,spot.pX,spot.pY) }
                        }

                    }
                }

                PlayerColors.GREEN->{
                    for (spot in place.spots){
                        when(spot.id){
                            1->{placeTokenOnSpot(binding.ivGreen1,spot.pX,spot.pY) }
                            2->{placeTokenOnSpot(binding.ivGreen2,spot.pX,spot.pY) }
                            3->{placeTokenOnSpot(binding.ivGreen3,spot.pX,spot.pY) }
                            4->{placeTokenOnSpot(binding.ivGreen4,spot.pX,spot.pY) }
                        }

                    }
                }

                PlayerColors.RED->{
                    for (spot in place.spots){
                        when(spot.id){
                            1->{placeTokenOnSpot(binding.ivRed1,spot.pX,spot.pY) }
                            2->{placeTokenOnSpot(binding.ivRed2,spot.pX,spot.pY) }
                            3->{placeTokenOnSpot(binding.ivRed3,spot.pX,spot.pY) }
                            4->{placeTokenOnSpot(binding.ivRed4,spot.pX,spot.pY) }
                        }

                    }
                }

                PlayerColors.BLUE->{
                    for (spot in place.spots){
                        when(spot.id){
                            1->{placeTokenOnSpot(binding.ivBlue1,spot.pX,spot.pY) }
                            2->{placeTokenOnSpot(binding.ivBlue2,spot.pX,spot.pY) }
                            3->{placeTokenOnSpot(binding.ivBlue3,spot.pX,spot.pY) }
                            4->{placeTokenOnSpot(binding.ivBlue4,spot.pX,spot.pY) }
                        }

                    }
                }
            }
        }
    }
    private fun placeDice(){
        for (spot in ludoMap.diceSpots){
            var dice : View
            when(spot.colors){
                PlayerColors.YELLOW->dice = binding.ivDice1
                PlayerColors.GREEN->dice = binding.ivDice2
                PlayerColors.RED->dice = binding.ivDice3
                PlayerColors.BLUE->dice = binding.ivDice4
                else->dice = binding.ivDice4
            }
//            placeDiceOnSpot(dice,spot.x1,spot.y1)
            placeDiceOnSpot(dice,spot.x1,spot.y1)
        }

    }
    private fun placeTokenOnSpot(view:View, x:Float, y:Float){
        view.layoutParams.height = (3*d/4).toInt()
        view.layoutParams.width = (3*d/4).toInt()

        val layoutParams =view.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.marginStart = (x-3*d/8).toInt()
        layoutParams.topMargin = (y-2*d/3).toInt()
        view.layoutParams = layoutParams
    }
    private fun placeDiceOnSpot(view:View, x:Float, y:Float){
        view.layoutParams.height = d.toInt()
        view.layoutParams.width = d.toInt()

        val layoutParams =view.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.marginStart = (x+d/5).toInt()
        layoutParams.topMargin = (y+d/5).toInt()
        view.layoutParams = layoutParams
    }


    private fun prepareDimensions(){
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        mHeight = resources.displayMetrics.heightPixels - resources.getDimensionPixelSize(resourceId)
        mWidth = resources.displayMetrics.widthPixels

        topSpacing = (mHeight-mWidth)/2
        bottomSpacing = (mHeight+mWidth)/2
        d = (mWidth / 15).toFloat()

        Log.i(TAG,"activity height : $mHeight")
        Log.i(TAG,"activity topSpacing : $topSpacing")
        Log.i(TAG,"activity d : $d")

        for (i in 0..15) column.add(d * i)
        for (j in 0..15) row.add(topSpacing + d * j)
    }

    // Map generation after this

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

        // preparing yellow token spots
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

        // preparing red token spots
        val rSpots = mutableListOf<Spot>()

        rSpots.add(Spot(1,column[x]+d/4,row[y]+d/3,d/3,PlayerColors.RED))
        rSpots.add(Spot(2,column[x+2]-d/4,row[y]+d/3,d/3,PlayerColors.RED))
        rSpots.add(Spot(3,column[x]+d/4,row[y+2]-d/3,d/3,PlayerColors.RED))
        rSpots.add(Spot(4,column[x+2]-d/4,row[y+2]-d/3,d/3,PlayerColors.RED))

        val rSpotBase = SpotBase(3,12,3,d*2)
        val rRestingPlace = RestingPlace(PlayerColors.RED,rSpotBase,9,0,rSpots)


        x=11
        y=11

        // preparing blue token spots
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