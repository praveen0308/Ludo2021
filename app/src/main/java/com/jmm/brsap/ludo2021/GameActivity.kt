package com.jmm.brsap.ludo2021

import android.graphics.drawable.AnimationDrawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.jmm.brsap.ludo2021.databinding.ActivityGameBinding
import com.jmm.brsap.ludo2021.models.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.StringBuilder

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
    private var row = mutableListOf<Float>()

    private val boardTiles = mutableListOf<Tile>()
    private val homePaths = mutableListOf<MutableList<Tile>>()
    private val restingPlaces = mutableListOf<RestingPlace>()
    private val dices = mutableListOf<Pair<PlayerColors, ImageView>>()

    private lateinit var activeColor: PlayerColors
    private val colors = hashMapOf<Int, PlayerColors>()

    private val TAG = "GameActivity"

    private val MOVEMENT_SPEED = 220L
    private val ELIMINATION_SPEED = 100L

    private var playersCount = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("PLAYERS")) playersCount = intent.getIntExtra("PLAYERS",2)

        prepareDimensions()
        prepareLudoMap()

        populateViews()
        subscribeObservers()

        binding.boardView.setLudoMap(ludoMap)
        placeTokens()
        placeDice()


        binding.apply {

            ivDice1.setOnClickListener {
                performDiceClick()
            }

            ivDice2.setOnClickListener {
                performDiceClick()
            }

            ivDice3.setOnClickListener {
                performDiceClick()
            }

            ivDice4.setOnClickListener {
                performDiceClick()
            }

            // yellow tokens

            ivYellow1.setOnClickListener {
                if (it.isClickable) moveToken(it, PlayerColors.YELLOW, PlayerColors.GREEN, 0, 0)
            }
            ivYellow2.setOnClickListener {
                if (it.isClickable) moveToken(it, PlayerColors.YELLOW, PlayerColors.GREEN, 0, 1)
            }
            ivYellow3.setOnClickListener {
                if (it.isClickable) moveToken(it, PlayerColors.YELLOW, PlayerColors.GREEN, 0, 2)
            }
            ivYellow4.setOnClickListener {
                if (it.isClickable) moveToken(it, PlayerColors.YELLOW, PlayerColors.GREEN, 0, 3)
            }

            // green tokens

            ivGreen1.setOnClickListener {
                if (it.isClickable) moveToken(it, PlayerColors.GREEN, PlayerColors.RED, 1, 0)
            }
            ivGreen2.setOnClickListener {
                if (it.isClickable) moveToken(it, PlayerColors.GREEN, PlayerColors.RED, 1, 1)
            }
            ivGreen3.setOnClickListener {
                if (it.isClickable) moveToken(it, PlayerColors.GREEN, PlayerColors.RED, 1, 2)
            }
            ivGreen4.setOnClickListener {
                if (it.isClickable) moveToken(it, PlayerColors.GREEN, PlayerColors.RED, 1, 3)
            }

            // red tokens

            ivRed1.setOnClickListener {
                if (it.isClickable) moveToken(it, PlayerColors.RED, PlayerColors.BLUE, 2, 0)
            }
            ivRed2.setOnClickListener {
                if (it.isClickable) moveToken(it, PlayerColors.RED, PlayerColors.BLUE, 2, 1)
            }
            ivRed3.setOnClickListener {
                if (it.isClickable) moveToken(it, PlayerColors.RED, PlayerColors.BLUE, 2, 2)
            }
            ivRed4.setOnClickListener {
                if (it.isClickable) moveToken(it, PlayerColors.RED, PlayerColors.BLUE, 2, 3)
            }

            // blue tokens

            ivBlue1.setOnClickListener {
                if (it.isClickable) moveToken(it, PlayerColors.BLUE, PlayerColors.YELLOW, 3, 0)
            }
            ivBlue2.setOnClickListener {
                if (it.isClickable) moveToken(it, PlayerColors.BLUE, PlayerColors.YELLOW, 3, 1)
            }
            ivBlue3.setOnClickListener {
                if (it.isClickable) moveToken(it, PlayerColors.BLUE, PlayerColors.YELLOW, 3, 2)
            }
            ivBlue4.setOnClickListener {
                if (it.isClickable) moveToken(it, PlayerColors.BLUE, PlayerColors.YELLOW, 3, 3)
            }

        }


    }

    private fun winToken(playerNo: Int,tokenNo: Int){
        when(playerNo){
            0->{
                when(tokenNo){
                    0->{

                    }
                    1->{}
                    2->{}
                    3->{}
                }
            }
            1->{}
            2->{}
            3->{}
        }
    }

    private fun moveToken(
        view: View,
        playerColor: PlayerColors,
        nextPlayerColor: PlayerColors,
        playerNo: Int,
        tokenNo: Int
    ) {
        if (ludoMap.players[playerNo].tokens[tokenNo].isFree) {
            lifecycleScope.launch {
                var eliminatedSomeone = false
                var stepsRemaining = ludoMap.players[playerNo].dice.outCome
                for (i in 1..ludoMap.players[playerNo].dice.outCome) {

                    // This token movement for home paths
                    if (ludoMap.players[playerNo].tokens[tokenNo].stepsCompleted >= 50) {
                        for (j in 1..stepsRemaining) {
                            val point =
                                ludoMap.players[playerNo].tokens[tokenNo].stepsCompleted - 50
                            placeTokenOnPath(
                                view,
                                ludoMap.homePaths[playerNo][point].column,
                                ludoMap.homePaths[playerNo][point].row
                            )
                            ludoMap.players[playerNo].tokens[tokenNo].stepsCompleted += 1
                            delay(MOVEMENT_SPEED)
                        }

                        break
                    }
                    // this way when
                    else {
                        val isLastStep = i == ludoMap.players[playerNo].dice.outCome
                        removeTokenFromTile(
                            ludoMap.players[playerNo].tokens[tokenNo].standingAt,
                            playerNo,
                            tokenNo
                        )

                        val point = ludoMap.players[playerNo].tokens[tokenNo].standingAt + 1

                        if (point > 51) {
                            val newPoint = point - 52
                            eliminatedSomeone =
                                enterTokenInTile(newPoint, playerNo, tokenNo, isLastStep)
                            placeTokenOnPath(
                                view,
                                ludoMap.tiles[newPoint].column,
                                ludoMap.tiles[newPoint].row
                            )
                            ludoMap.players[playerNo].tokens[tokenNo].standingAt = newPoint

                        } else {
                            eliminatedSomeone =
                                enterTokenInTile(point, playerNo, tokenNo, isLastStep)
                            placeTokenOnPath(
                                view,
                                ludoMap.tiles[point].column,
                                ludoMap.tiles[point].row
                            )
                            ludoMap.players[playerNo].tokens[tokenNo].standingAt = point
                        }
                        ludoMap.players[playerNo].tokens[tokenNo].stepsCompleted += 1
                        stepsRemaining--
                        delay(MOVEMENT_SPEED)
                    }
                }


                if (eliminatedSomeone) {
                    viewModel.activeColor.postValue(playerColor)
                } else {
                    if (ludoMap.players[playerNo].dice.outCome == 6) viewModel.activeColor.postValue(
                        playerColor
                    )
                    else viewModel.activeColor.postValue(nextPlayerColor)
                }

            }


        } else {
            val startingPoint = ludoMap.players[playerNo].tokens[tokenNo].startingFrom
            placeTokenOnPath(
                view,
                ludoMap.tiles[startingPoint].column,
                ludoMap.tiles[startingPoint].row
            )
            ludoMap.players[playerNo].tokens[tokenNo].isFree = true
            ludoMap.players[playerNo].tokens[tokenNo].standingAt = startingPoint

            enterTokenInTile(startingPoint, playerNo, tokenNo, false)
            viewModel.activeColor.postValue(playerColor)
        }

    }

    private fun removeTokenFromTile(tileNo: Int, playerNo: Int, tokenNo: Int) {
        for ((index, token) in ludoMap.tiles[tileNo].tokens.withIndex()) {
            if (token.playerNo == playerNo && token.tokenNo == tokenNo) {
                ludoMap.tiles[tileNo].tokens.removeAt(index)
                Log.d(
                    TAG,
                    "Token no. ${token.tokenNo} of Player ${token.playerNo} is removed from tile no $tileNo."
                )
            }
        }
    }

    private fun enterTokenInTile(
        tileNo: Int,
        playerNo: Int,
        tokenNo: Int,
        isLastStep: Boolean
    ): Boolean {
        var eliminatedSomeone = false
        if (isLastStep) {
            if (ludoMap.tiles[tileNo].tokens.isEmpty() || ludoMap.tiles[tileNo].isStop) {
                ludoMap.tiles[tileNo].tokens.add(ludoMap.players[playerNo].tokens[tokenNo])
                Log.d(TAG, "Token no. $tokenNo of Player $playerNo is added into tile no $tileNo.")
            } else {

                for (token in ludoMap.tiles[tileNo].tokens) {
                    if (token.color != ludoMap.players[playerNo].color) {

                        removeTokenFromTile(tileNo, token.playerNo, token.tokenNo)
                        resetToken(token.playerNo, token.tokenNo)
                        eliminatedSomeone = true
                        Log.d(
                            TAG,
                            "Token $tokenNo of Player $playerNo eliminated token ${token.tokenNo} of Player ${token.playerNo}"
                        )
                    }
                }
                ludoMap.tiles[tileNo].tokens.add(ludoMap.players[playerNo].tokens[tokenNo])
                Log.d(TAG, "Token no. $tokenNo of Player $playerNo is added into tile no $tileNo.")
            }
        } else {
            ludoMap.tiles[tileNo].tokens.add(ludoMap.players[playerNo].tokens[tokenNo])
            Log.d(TAG, "Token no. $tokenNo of Player $playerNo is added into tile no $tileNo.")
        }

        return eliminatedSomeone
    }

    private fun resetToken(playerNo: Int, tokenNo: Int) {

        var stepsCompleted = ludoMap.players[playerNo].tokens[tokenNo].stepsCompleted

        lifecycleScope.launch {
            for (i in stepsCompleted.downTo(1)){
                var point = ludoMap.players[playerNo].tokens[tokenNo].standingAt
                if (point==0){
                    ludoMap.players[playerNo].tokens[tokenNo].standingAt = 51
                    point = ludoMap.players[playerNo].tokens[tokenNo].standingAt
                }
                else{
                    point--
                }
                ludoMap.players[playerNo].tokens[tokenNo].standingAt = point
                placeTokenOnPath(ludoMap.players[playerNo].tokens[tokenNo].tokenImage,ludoMap.tiles[point].column,
                    ludoMap.tiles[point].row)
                delay(ELIMINATION_SPEED)
            }

            placeTokenOnSpot(
                ludoMap.players[playerNo].tokens[tokenNo].tokenImage,
                ludoMap.restingPlaces[playerNo].spots[tokenNo].pX,
                ludoMap.restingPlaces[playerNo].spots[tokenNo].pY
            )

            ludoMap.players[playerNo].tokens[tokenNo].stepsCompleted = 0
            ludoMap.players[playerNo].tokens[tokenNo].standingAt = -1
            ludoMap.players[playerNo].tokens[tokenNo].isFree = false
            ludoMap.players[playerNo].tokens[tokenNo].canMove = false
        }

    }

    private fun subscribeObservers() {
        viewModel.activeColor.observe(this, { color ->
            /*
                1. show dice for player according to color
                2. check for token is free
                    if diceNo. = 6 then active all token
                    else

            */
            activeColor = color

            when (activeColor) {
                PlayerColors.YELLOW -> {
                    viewModel.yellowDiceState.postValue(DiceState.READY)
                    viewModel.greenDiceState.postValue(DiceState.REST)
                    viewModel.redDiceState.postValue(DiceState.REST)
                    viewModel.blueDiceState.postValue(DiceState.REST)

                }
                PlayerColors.GREEN -> {
                    viewModel.yellowDiceState.postValue(DiceState.REST)
                    viewModel.greenDiceState.postValue(DiceState.READY)
                    viewModel.redDiceState.postValue(DiceState.REST)
                    viewModel.blueDiceState.postValue(DiceState.REST)
                }
                PlayerColors.RED -> {
                    viewModel.yellowDiceState.postValue(DiceState.REST)
                    viewModel.greenDiceState.postValue(DiceState.REST)
                    viewModel.redDiceState.postValue(DiceState.READY)
                    viewModel.blueDiceState.postValue(DiceState.REST)
                }
                PlayerColors.BLUE -> {
                    viewModel.yellowDiceState.postValue(DiceState.REST)
                    viewModel.greenDiceState.postValue(DiceState.REST)
                    viewModel.redDiceState.postValue(DiceState.REST)
                    viewModel.blueDiceState.postValue(DiceState.READY)
                }
            }

        })

        viewModel.yellowDiceState.observe(this, { state ->
            onDiceStateChanged(0, state, PlayerColors.GREEN)
        })
        viewModel.greenDiceState.observe(this, { state ->
            onDiceStateChanged(1, state, PlayerColors.RED)
        })

        viewModel.redDiceState.observe(this, { state ->
            onDiceStateChanged(2, state, PlayerColors.BLUE)
        })

        viewModel.blueDiceState.observe(this, { state ->
            onDiceStateChanged(3, state, PlayerColors.YELLOW)
        })

    }

    private fun onDiceStateChanged(playerNo: Int, state: DiceState, nextPlayerColor: PlayerColors) {

        ludoMap.players[playerNo].dice.state = state

        val isAnyOneFree = ludoMap.players[playerNo].tokens.any { it.isFree }


        for (tokenNo in 0..3) {
            ludoMap.players[playerNo].tokens[tokenNo].tokenImage.apply {
                if (state == DiceState.WAITING) {
                    val outCome = ludoMap.players[playerNo].dice.outCome
                    if ((ludoMap.players[playerNo].tokens[tokenNo].stepsCompleted + outCome) > 56) {

                        lifecycleScope.launch {
                            delay(700)
                            viewModel.activeColor.postValue(nextPlayerColor)
                        }


                    } else {
                        if (outCome == 6) {

                            this.isClickable = true
                            this.isMovable(true)
                        } else {
                            if (ludoMap.players[playerNo].tokens[tokenNo].isFree) {
                                this.isClickable = true
                                this.isMovable(true)
                            } else {
                                this.isClickable = false
                                this.isMovable(false)

                            }
                        }
                    }

                } else {
                    isClickable = false
                    this.isMovable(false)
                }
            }
        }
        if (state == DiceState.WAITING) {
            if (ludoMap.players[playerNo].dice.outCome != 6) {
                if (!isAnyOneFree) {
                    lifecycleScope.launch {
                        delay(700)
                        viewModel.activeColor.postValue(nextPlayerColor)
                    }
                }
            }
        }

        when (state) {
            DiceState.READY -> {
                ludoMap.players[playerNo].dice.diceView.isVisible = true
                ludoMap.players[playerNo].dice.diceView.isClickable = true

            }
            DiceState.WAITING -> {
                ludoMap.players[playerNo].dice.diceView.isVisible = true
                ludoMap.players[playerNo].dice.diceView.isClickable = false

            }
            DiceState.SPINNING -> {
                ludoMap.players[playerNo].dice.diceView.isVisible = true
                ludoMap.players[playerNo].dice.diceView.isClickable = false
            }
            DiceState.IDLE -> {
                ludoMap.players[playerNo].dice.diceView.isVisible = true
                ludoMap.players[playerNo].dice.diceView.isClickable = false
            }
            DiceState.REST -> {
                ludoMap.players[playerNo].dice.diceView.isVisible = false
                ludoMap.players[playerNo].dice.diceView.isClickable = false
            }
        }

    }


    private fun performDiceClick() {
        when (activeColor) {
            PlayerColors.YELLOW -> {
                val playerNo = 0
                viewModel.yellowDiceState.postValue(DiceState.SPINNING)
//                ludoMap.players[0].dice.diceView.setBackgroundResource(R.drawable.green_dice_animation)
//                val diceAnimation =ludoMap.players[0].dice.diceView.background as AnimationDrawable

                ludoMap.players[playerNo].dice.diceView.animate()
                    .scaleX(1.5f)
                    .scaleY(1.5f)
                    .rotation(-45f)
                    .rotationY(1080f).withStartAction {
//                        diceAnimation.start()
                    }.withEndAction {
                        ludoMap.players[playerNo].dice.diceView.scaleX = 1f
                        ludoMap.players[playerNo].dice.diceView.scaleY = 1f
                        ludoMap.players[playerNo].dice.diceView.rotation = 0f
                        ludoMap.players[playerNo].dice.diceView.rotationY = 0f


                        val outcome = (1..6).random()
                        ludoMap.players[playerNo].dice.outCome = outcome
                        ludoMap.players[playerNo].dice.diceView.setImageResource(
                            getRandomDiceFace(
                                activeColor,
                                outcome
                            )
                        )
                        viewModel.yellowDiceState.postValue(DiceState.WAITING)

                    }
            }
            PlayerColors.GREEN -> {
                val playerNo = 1
                viewModel.greenDiceState.postValue(DiceState.SPINNING)
                ludoMap.players[playerNo].dice.diceView.animate()
                    .scaleX(1.5f)
                    .scaleY(1.5f)
                    .rotation(-45f)
                    .rotationY(1080f).withEndAction {
                        ludoMap.players[playerNo].dice.diceView.scaleX = 1f
                        ludoMap.players[playerNo].dice.diceView.scaleY = 1f
                        ludoMap.players[playerNo].dice.diceView.rotation = 0f
                        ludoMap.players[playerNo].dice.diceView.rotationY = 0f


                        val outcome = (1..6).random()
                        ludoMap.players[playerNo].dice.outCome = outcome
                        ludoMap.players[playerNo].dice.diceView.setImageResource(
                            getRandomDiceFace(
                                activeColor,
                                outcome
                            )
                        )
                        viewModel.greenDiceState.postValue(DiceState.WAITING)

                    }
            }
            PlayerColors.RED -> {
                val playerNo = 2
                viewModel.redDiceState.postValue(DiceState.SPINNING)
                ludoMap.players[playerNo].dice.diceView.animate()
                    .scaleX(1.5f)
                    .scaleY(1.5f)
                    .rotation(-45f)
                    .rotationY(1080f).withEndAction {
                        ludoMap.players[playerNo].dice.diceView.scaleX = 1f
                        ludoMap.players[playerNo].dice.diceView.scaleY = 1f
                        ludoMap.players[playerNo].dice.diceView.rotation = 0f
                        ludoMap.players[playerNo].dice.diceView.rotationY = 0f


                        val outcome = (1..6).random()
                        ludoMap.players[playerNo].dice.outCome = outcome
                        ludoMap.players[playerNo].dice.diceView.setImageResource(
                            getRandomDiceFace(
                                activeColor,
                                outcome
                            )
                        )
                        viewModel.redDiceState.postValue(DiceState.WAITING)

                    }
            }
            PlayerColors.BLUE -> {
                val playerNo = 3
                viewModel.blueDiceState.postValue(DiceState.SPINNING)
                ludoMap.players[playerNo].dice.diceView.animate()
                    .scaleX(1.5f)
                    .scaleY(1.5f)
                    .rotation(-45f)
                    .rotationY(1080f).withEndAction {
                        ludoMap.players[playerNo].dice.diceView.scaleX = 1f
                        ludoMap.players[playerNo].dice.diceView.scaleY = 1f
                        ludoMap.players[playerNo].dice.diceView.rotation = 0f
                        ludoMap.players[playerNo].dice.diceView.rotationY = 0f


                        val outcome = (1..6).random()
                        ludoMap.players[playerNo].dice.outCome = outcome
                        ludoMap.players[playerNo].dice.diceView.setImageResource(
                            getRandomDiceFace(
                                activeColor,
                                outcome
                            )
                        )
                        viewModel.blueDiceState.postValue(DiceState.WAITING)

                    }
            }
        }


    }


    private fun placeTokenOnPath(view: View, x: Int, y: Int) {
        view.layoutParams.height = (3 * d / 4).toInt()
        view.layoutParams.width = (3 * d / 4).toInt()

        val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.marginStart = (column[x - 1] + d / 10).toInt()
        layoutParams.topMargin = (row[y - 1] + d / 10).toInt()
        view.layoutParams = layoutParams
    }

    private fun getRandomDiceFace(color: PlayerColors, index: Int): Int {
        val yellowDiceFaces = HashMap<Int, Int>()
        yellowDiceFaces[1] = R.drawable.yellow_face_1
        yellowDiceFaces[2] = R.drawable.yellow_face_2
        yellowDiceFaces[3] = R.drawable.yellow_face_3
        yellowDiceFaces[4] = R.drawable.yellow_face_4
        yellowDiceFaces[5] = R.drawable.yellow_face_5
        yellowDiceFaces[6] = R.drawable.yellow_face_6


        val greenDiceFaces = HashMap<Int, Int>()
        greenDiceFaces[1] = R.drawable.green_face_1
        greenDiceFaces[2] = R.drawable.green_face_2
        greenDiceFaces[3] = R.drawable.green_face_3
        greenDiceFaces[4] = R.drawable.green_face_4
        greenDiceFaces[5] = R.drawable.green_face_5
        greenDiceFaces[6] = R.drawable.green_face_6


        val redDiceFaces = HashMap<Int, Int>()
        redDiceFaces[1] = R.drawable.red_face_1
        redDiceFaces[2] = R.drawable.red_face_2
        redDiceFaces[3] = R.drawable.red_face_3
        redDiceFaces[4] = R.drawable.red_face_4
        redDiceFaces[5] = R.drawable.red_face_5
        redDiceFaces[6] = R.drawable.red_face_6

        val blueDiceFaces = HashMap<Int, Int>()
        blueDiceFaces[1] = R.drawable.blue_face_1
        blueDiceFaces[2] = R.drawable.blue_face_2
        blueDiceFaces[3] = R.drawable.blue_face_3
        blueDiceFaces[4] = R.drawable.blue_face_4
        blueDiceFaces[5] = R.drawable.blue_face_5
        blueDiceFaces[6] = R.drawable.blue_face_6

        return when (color) {
            PlayerColors.YELLOW -> {
                yellowDiceFaces[index]!!
            }
            PlayerColors.GREEN -> {
                greenDiceFaces[index]!!
            }
            PlayerColors.RED -> {
                redDiceFaces[index]!!
            }
            PlayerColors.BLUE -> {
                blueDiceFaces[index]!!
            }
            else -> 0
        }


    }

    private fun populateViews() {

        colors[1] = PlayerColors.YELLOW
        colors[2] = PlayerColors.GREEN
        colors[3] = PlayerColors.RED
        colors[4] = PlayerColors.BLUE

        dices.add(Pair(PlayerColors.YELLOW, binding.ivDice1))
        dices.add(Pair(PlayerColors.GREEN, binding.ivDice2))
        dices.add(Pair(PlayerColors.RED, binding.ivDice3))
        dices.add(Pair(PlayerColors.BLUE, binding.ivDice4))


    }

    private fun prepareLudoMap() {
        generateRestingPlaces()
        generateTiles()
        generateHomePaths()

        val diceSpots = mutableListOf<DiceSpot>()
        diceSpots.add(
            DiceSpot(
                d / 10,
                topSpacing + 15 * d,
                3 * d / 2,
                topSpacing + 15 * d + 3 * d / 2,
                PlayerColors.YELLOW,
                d / 5
            )
        )
        diceSpots.add(
            DiceSpot(
                d / 10,
                topSpacing - 3 * d / 2,
                3 * d / 2,
                topSpacing * 1f,
                PlayerColors.GREEN,
                d / 5
            )
        )
        diceSpots.add(
            DiceSpot(
                14 * d - d / 2,
                topSpacing - 3 * d / 2,
                14 * d + 2 * d / 2 - d / 10,
                topSpacing * 1f,
                PlayerColors.RED,
                d / 5
            )
        )
        diceSpots.add(
            DiceSpot(
                14 * d - d / 2,
                topSpacing + 15 * d,
                14 * d + 2 * d / 2 - d / 10,
                topSpacing + 15 * d + 3 * d / 2,
                PlayerColors.BLUE,
                d / 5
            )
        )


        val players = mutableListOf<Player>()
        val yellowTokens = mutableListOf<Token>()
        yellowTokens.add(Token(0, PlayerColors.YELLOW, 0, 0, binding.ivYellow1))
        yellowTokens.add(Token(1, PlayerColors.YELLOW, 0, 0, binding.ivYellow2))
        yellowTokens.add(Token(2, PlayerColors.YELLOW, 0, 0, binding.ivYellow3))
        yellowTokens.add(Token(3, PlayerColors.YELLOW, 0, 0, binding.ivYellow4))
        val yellowDice = Dice(PlayerColors.YELLOW, DiceState.READY, binding.ivDice1)
        players.add(
            Player(
                0,
                "Player 1",
                PlayerColors.YELLOW,
                PlayerNumbers.PLAYER_1,
                tokens = yellowTokens,
                dice = yellowDice
            )
        )


        val greenTokens = mutableListOf<Token>()
        greenTokens.add(Token(0, PlayerColors.GREEN, 1, 13, binding.ivGreen1))
        greenTokens.add(Token(1, PlayerColors.GREEN, 1, 13, binding.ivGreen2))
        greenTokens.add(Token(2, PlayerColors.GREEN, 1, 13, binding.ivGreen3))
        greenTokens.add(Token(3, PlayerColors.GREEN, 1, 13, binding.ivGreen4))
        val greenDice = Dice(PlayerColors.GREEN, DiceState.READY, binding.ivDice2)
        players.add(
            Player(
                1,
                "Player 2",
                PlayerColors.GREEN,
                PlayerNumbers.PLAYER_2,
                tokens = greenTokens,
                dice = greenDice
            )
        )


        val redTokens = mutableListOf<Token>()
        redTokens.add(Token(0, PlayerColors.RED, 2, 26, binding.ivRed1))
        redTokens.add(Token(1, PlayerColors.RED, 2, 26, binding.ivRed2))
        redTokens.add(Token(2, PlayerColors.RED, 2, 26, binding.ivRed3))
        redTokens.add(Token(3, PlayerColors.RED, 2, 26, binding.ivRed4))
        val redDice = Dice(PlayerColors.RED, DiceState.READY, binding.ivDice3)
        players.add(
            Player(
                2,
                "Player 3",
                PlayerColors.RED,
                PlayerNumbers.PLAYER_3,
                tokens = redTokens,
                dice = redDice
            )
        )


        val blueTokens = mutableListOf<Token>()
        blueTokens.add(Token(0, PlayerColors.BLUE, 3, 39, binding.ivBlue1))
        blueTokens.add(Token(1, PlayerColors.BLUE, 3, 39, binding.ivBlue2))
        blueTokens.add(Token(2, PlayerColors.BLUE, 3, 39, binding.ivBlue3))
        blueTokens.add(Token(3, PlayerColors.BLUE, 3, 39, binding.ivBlue4))

        val blueDice = Dice(PlayerColors.BLUE, DiceState.READY, binding.ivDice4)

        players.add(
            Player(
                3,
                "Player 4",
                PlayerColors.BLUE,
                PlayerNumbers.PLAYER_4,
                tokens = blueTokens,
                dice = blueDice
            )
        )

        ludoMap = LudoMap(
            100,
            PlayersCount.FOUR_PLAYER,
            players,
            restingPlaces,
            boardTiles,
            homePaths,
            diceSpots
        )
    }

    private fun placeTokens() {
        for (place in ludoMap.restingPlaces) {
            when (place.color) {
                PlayerColors.YELLOW -> {
                    for (spot in place.spots) {
                        when (spot.id) {
                            1 -> {
                                placeTokenOnSpot(binding.ivYellow1, spot.pX, spot.pY)
                            }
                            2 -> {
                                placeTokenOnSpot(binding.ivYellow2, spot.pX, spot.pY)
                            }
                            3 -> {
                                placeTokenOnSpot(binding.ivYellow3, spot.pX, spot.pY)
                            }
                            4 -> {
                                placeTokenOnSpot(binding.ivYellow4, spot.pX, spot.pY)
                            }
                        }

                    }
                }

                PlayerColors.GREEN -> {
                    for (spot in place.spots) {
                        when (spot.id) {
                            1 -> {
                                placeTokenOnSpot(binding.ivGreen1, spot.pX, spot.pY)
                            }
                            2 -> {
                                placeTokenOnSpot(binding.ivGreen2, spot.pX, spot.pY)
                            }
                            3 -> {
                                placeTokenOnSpot(binding.ivGreen3, spot.pX, spot.pY)
                            }
                            4 -> {
                                placeTokenOnSpot(binding.ivGreen4, spot.pX, spot.pY)
                            }
                        }

                    }
                }

                PlayerColors.RED -> {
                    for (spot in place.spots) {
                        when (spot.id) {
                            1 -> {
                                placeTokenOnSpot(binding.ivRed1, spot.pX, spot.pY)
                            }
                            2 -> {
                                placeTokenOnSpot(binding.ivRed2, spot.pX, spot.pY)
                            }
                            3 -> {
                                placeTokenOnSpot(binding.ivRed3, spot.pX, spot.pY)
                            }
                            4 -> {
                                placeTokenOnSpot(binding.ivRed4, spot.pX, spot.pY)
                            }
                        }

                    }
                }

                PlayerColors.BLUE -> {
                    for (spot in place.spots) {
                        when (spot.id) {
                            1 -> {
                                placeTokenOnSpot(binding.ivBlue1, spot.pX, spot.pY)
                            }
                            2 -> {
                                placeTokenOnSpot(binding.ivBlue2, spot.pX, spot.pY)
                            }
                            3 -> {
                                placeTokenOnSpot(binding.ivBlue3, spot.pX, spot.pY)
                            }
                            4 -> {
                                placeTokenOnSpot(binding.ivBlue4, spot.pX, spot.pY)
                            }
                        }

                    }
                }
            }
        }
    }

    private fun placeDice() {
        for (spot in ludoMap.diceSpots) {
            var dice: View
            when (spot.colors) {
                PlayerColors.YELLOW -> dice = binding.ivDice1
                PlayerColors.GREEN -> dice = binding.ivDice2
                PlayerColors.RED -> dice = binding.ivDice3
                PlayerColors.BLUE -> dice = binding.ivDice4
                else -> dice = binding.ivDice4
            }

            placeDiceOnSpot(dice, spot.x1, spot.y1)
        }

    }

    private fun placeTokenOnSpot(view: View, x: Float, y: Float) {
        view.layoutParams.height = (3 * d / 4).toInt()
        view.layoutParams.width = (3 * d / 4).toInt()

        val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.marginStart = (x - 3 * d / 8).toInt()
        layoutParams.topMargin = (y - 2 * d / 3).toInt()
        view.layoutParams = layoutParams
    }

    private fun placeDiceOnSpot(view: View, x: Float, y: Float) {
        view.layoutParams.height = d.toInt()
        view.layoutParams.width = d.toInt()

        val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.marginStart = (x + d / 5).toInt()
        layoutParams.topMargin = (y + d / 5).toInt()
        view.layoutParams = layoutParams
    }

    private fun prepareDimensions() {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        mHeight =
            resources.displayMetrics.heightPixels - resources.getDimensionPixelSize(resourceId)
        mWidth = resources.displayMetrics.widthPixels

        topSpacing = (mHeight - mWidth) / 2
        bottomSpacing = (mHeight + mWidth) / 2
        d = (mWidth / 15).toFloat()

        Log.i(TAG, "activity height : $mHeight")
        Log.i(TAG, "activity topSpacing : $topSpacing")
        Log.i(TAG, "activity d : $d")

        for (i in 0..15) column.add(d * i)
        for (j in 0..15) row.add(topSpacing + d * j)
    }

// Map generation after this

    private fun generateHomePaths() {
        val yPaths = mutableListOf<Tile>()
        for (i in 14 downTo 10) {
            yPaths.add(Tile(1, 8, i, false, PlayerColors.YELLOW))
        }

        val gPaths = mutableListOf<Tile>()
        for (i in 2..6) {
            gPaths.add(Tile(2, i, 8, false, PlayerColors.GREEN))
        }

        val rPaths = mutableListOf<Tile>()
        for (i in 2..6) {
            rPaths.add(Tile(3, 8, i, false, PlayerColors.RED))
        }

        val bPaths = mutableListOf<Tile>()
        for (i in 14 downTo 10) {
            bPaths.add(Tile(4, i, 8, false, PlayerColors.BLUE))
        }

        homePaths.add(yPaths)
        homePaths.add(gPaths)
        homePaths.add(rPaths)
        homePaths.add(bPaths)

    }

    private fun generateTiles() {
        var tile: Tile
        for (i in 0..51) {
            when (i) {
                0 -> {
                    tile = Tile(i, 7, 14, true, PlayerColors.YELLOW)
                }
                1 -> {
                    tile = Tile(i, 7, 13)
                }
                2 -> {
                    tile = Tile(i, 7, 12)
                }
                3 -> {
                    tile = Tile(i, 7, 11)
                }
                4 -> {
                    tile = Tile(i, 7, 10)
                }
                5 -> {
                    tile = Tile(i, 6, 9)
                }
                6 -> {
                    tile = Tile(i, 5, 9)
                }
                7 -> {
                    tile = Tile(i, 4, 9)
                }
                8 -> {
                    tile = Tile(i, 3, 9, true)
                }
                9 -> {
                    tile = Tile(i, 2, 9)
                }
                10 -> {
                    tile = Tile(i, 1, 9)
                }
                11 -> {
                    tile = Tile(i, 1, 8, false, PlayerColors.WHITE, PlayerColors.GREEN)
                }
                12 -> {
                    tile = Tile(i, 1, 7)
                }
                13 -> {
                    tile = Tile(i, 2, 7, true, PlayerColors.GREEN)
                }
                14 -> {
                    tile = Tile(i, 3, 7)
                }
                15 -> {
                    tile = Tile(i, 4, 7)
                }
                16 -> {
                    tile = Tile(i, 5, 7)
                }
                17 -> {
                    tile = Tile(i, 6, 7)
                }
                18 -> {
                    tile = Tile(i, 7, 6)
                }
                19 -> {
                    tile = Tile(i, 7, 5)
                }
                20 -> {
                    tile = Tile(i, 7, 4)
                }
                21 -> {
                    tile = Tile(i, 7, 3, true)
                }
                22 -> {
                    tile = Tile(i, 7, 2)
                }
                23 -> {
                    tile = Tile(i, 7, 1)
                }
                24 -> {
                    tile = Tile(i, 8, 1, false, PlayerColors.WHITE, PlayerColors.RED)
                }
                25 -> {
                    tile = Tile(i, 9, 1)
                }
                26 -> {
                    tile = Tile(i, 9, 2, true, PlayerColors.RED)
                }
                27 -> {
                    tile = Tile(i, 9, 3)
                }
                28 -> {
                    tile = Tile(i, 9, 4)
                }
                29 -> {
                    tile = Tile(i, 9, 5)
                }
                30 -> {
                    tile = Tile(i, 9, 6)
                }
                31 -> {
                    tile = Tile(i, 10, 7)
                }
                32 -> {
                    tile = Tile(i, 11, 7)
                }
                33 -> {
                    tile = Tile(i, 12, 7)
                }
                34 -> {
                    tile = Tile(i, 13, 7, true)
                }
                35 -> {
                    tile = Tile(i, 14, 7)
                }
                36 -> {
                    tile = Tile(i, 15, 7)
                }
                37 -> {
                    tile = Tile(i, 15, 8, false, PlayerColors.WHITE, PlayerColors.BLUE)
                }
                38 -> {
                    tile = Tile(i, 15, 9)
                }
                39 -> {
                    tile = Tile(i, 14, 9, true, PlayerColors.BLUE)
                }
                40 -> {
                    tile = Tile(i, 13, 9)
                }
                41 -> {
                    tile = Tile(i, 12, 9)
                }
                42 -> {
                    tile = Tile(i, 11, 9)
                }
                43 -> {
                    tile = Tile(i, 10, 9)
                }
                44 -> {
                    tile = Tile(i, 9, 10)
                }
                45 -> {
                    tile = Tile(i, 9, 11)
                }
                46 -> {
                    tile = Tile(i, 9, 12)
                }
                47 -> {
                    tile = Tile(i, 9, 13, true)
                }
                48 -> {
                    tile = Tile(i, 9, 14)
                }
                49 -> {
                    tile = Tile(i, 9, 15)
                }
                50 -> {
                    tile = Tile(i, 8, 15, false, PlayerColors.WHITE, PlayerColors.YELLOW)
                }
                51 -> {
                    tile = Tile(i, 7, 15)
                }
                else -> {
                    tile = Tile(i, i, i)
                }
            }

            boardTiles.add(tile)
        }

    }

    private fun generateRestingPlaces() {
        var x = 2
        var y = 11

        // preparing yellow token spots
        val ySpots = mutableListOf<Spot>()

        ySpots.add(Spot(1, column[x] + d / 4, row[y] + d / 3, d / 3, PlayerColors.YELLOW))
        ySpots.add(Spot(2, column[x + 2] - d / 4, row[y] + d / 3, d / 3, PlayerColors.YELLOW))
        ySpots.add(Spot(3, column[x] + d / 4, row[y + 2] - d / 3, d / 3, PlayerColors.YELLOW))
        ySpots.add(Spot(4, column[x + 2] - d / 4, row[y + 2] - d / 3, d / 3, PlayerColors.YELLOW))

        val ySpotBase = SpotBase(1, 3, 12, d * 2)
        val yRestingPlace = RestingPlace(PlayerColors.YELLOW, ySpotBase, 0, 9, ySpots)

        x = 2
        y = 2

        // preparing green token spots
        val gSpots = mutableListOf<Spot>()

        gSpots.add(Spot(1, column[x] + d / 4, row[y] + d / 3, d / 3, PlayerColors.GREEN))
        gSpots.add(Spot(2, column[x + 2] - d / 4, row[y] + d / 3, d / 3, PlayerColors.GREEN))
        gSpots.add(Spot(3, column[x] + d / 4, row[y + 2] - d / 3, d / 3, PlayerColors.GREEN))
        gSpots.add(Spot(4, column[x + 2] - d / 4, row[y + 2] - d / 3, d / 3, PlayerColors.GREEN))

        val gSpotBase = SpotBase(2, 3, 3, d * 2)
        val gRestingPlace = RestingPlace(PlayerColors.GREEN, gSpotBase, 0, 0, gSpots)

        x = 11
        y = 2

        // preparing red token spots
        val rSpots = mutableListOf<Spot>()

        rSpots.add(Spot(1, column[x] + d / 4, row[y] + d / 3, d / 3, PlayerColors.RED))
        rSpots.add(Spot(2, column[x + 2] - d / 4, row[y] + d / 3, d / 3, PlayerColors.RED))
        rSpots.add(Spot(3, column[x] + d / 4, row[y + 2] - d / 3, d / 3, PlayerColors.RED))
        rSpots.add(Spot(4, column[x + 2] - d / 4, row[y + 2] - d / 3, d / 3, PlayerColors.RED))

        val rSpotBase = SpotBase(3, 12, 3, d * 2)
        val rRestingPlace = RestingPlace(PlayerColors.RED, rSpotBase, 9, 0, rSpots)


        x = 11
        y = 11

        // preparing blue token spots
        val bSpots = mutableListOf<Spot>()
        bSpots.add(Spot(1, column[x] + d / 4, row[y] + d / 3, d / 3, PlayerColors.BLUE))
        bSpots.add(Spot(2, column[x + 2] - d / 4, row[y] + d / 3, d / 3, PlayerColors.BLUE))
        bSpots.add(Spot(3, column[x] + d / 4, row[y + 2] - d / 3, d / 3, PlayerColors.BLUE))
        bSpots.add(Spot(4, column[x + 2] - d / 4, row[y + 2] - d / 3, d / 3, PlayerColors.BLUE))

        val bSpotBase = SpotBase(4, 12, 12, d * 2)
        val bRestingPlace = RestingPlace(PlayerColors.BLUE, bSpotBase, 9, 9, bSpots)

        restingPlaces.add(yRestingPlace)
        restingPlaces.add(gRestingPlace)
        restingPlaces.add(rRestingPlace)
        restingPlaces.add(bRestingPlace)
    }


}