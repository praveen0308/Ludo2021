package com.jmm.brsap.ludo2021

import android.content.Context
import android.graphics.*
import android.graphics.Shader.TileMode
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.jmm.brsap.ludo2021.models.LudoMap
import com.jmm.brsap.ludo2021.models.PlayerColors
import com.jmm.brsap.ludo2021.models.RestingPlace


class BoardViewType3(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    // Painter
    private lateinit var borderPaint: Paint
    private lateinit var redPaint: Paint
    private lateinit var whitePaint: Paint
    private lateinit var greenPaint: Paint
    private lateinit var yellowPaint: Paint
    private lateinit var bluePaint: Paint


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

    // screen matrix
    private val _matrix = mutableListOf<MutableList<Pair<Float, Float>>>()
    private val column = mutableListOf<Float>()
    private val row = mutableListOf<Float>()
    private val screenRows = mutableListOf<Float>()

    private lateinit var ludoMap: LudoMap


    init {

        initializePainters()

    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        prepareScreenDimensions()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)

        populateDisplayMatrix()

        createBoard(ludoMap,canvas)
        Log.i("testing","height : $mHeight")
        Log.i("testing","topSpacing : $topSpacing")
        Log.i("testing","d : $d")

        // generating dice spots
        for (spot in ludoMap.diceSpots){
            createRoundRect(spot.x1,spot.y1,spot.x2,spot.y2,canvas,R.color.start_wood,R.color.end_wood,d/5)
        }


/*        // dice1
        createRoundRect(d/10,topSpacing+15*d,3*d/2,topSpacing+15*d+3*d/2,canvas,R.color.start_wood,R.color.end_wood,d/5)

        // dice2
        createRoundRect(d/10,topSpacing-3*d/2,3*d/2,topSpacing*1f,canvas,R.color.start_wood,R.color.end_wood,d/5)
        // dice3
        createRoundRect(14*d-d/2,topSpacing-3*d/2,14*d+2*d/2-d/10,topSpacing*1f,canvas,R.color.start_wood,R.color.end_wood,d/5)

        // dice4
        createRoundRect(14*d-d/2,topSpacing+15*d,14*d+2*d/2-d/10,topSpacing+15*d+3*d/2,canvas,R.color.start_wood,R.color.end_wood,d/5)*/

    }

    public fun getDSize() = d

    private fun prepareScreenDimensions() {
        mWidth = width
        mHeight = height

        d = (width / 15).toFloat()
        /*
        * This for making map 15x15 square shaped
        * */
        topSpacing = (mHeight - mWidth) / 2
        bottomSpacing = (mHeight + mWidth) / 2

        cellPadding = d / 15
        tileRadius = d / 10
        restingRoomPadding = d / 5
        restingPlaceRadius = d / 3


    }

    fun setLudoMap(ludoMap: LudoMap) {
        this.ludoMap = ludoMap



    }

    private fun initializePainters() {
        borderPaint = Paint()

        borderPaint.apply {
            color = Color.BLACK
            strokeWidth = 2f
            style = Paint.Style.STROKE
            isAntiAlias = false
        }


        redPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        redPaint.apply {
            strokeWidth = 5F
            color = ContextCompat.getColor(context, R.color.end_red)
            isAntiAlias = true
            style = Paint.Style.STROKE
        }

        whitePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        whitePaint.apply {
            color = Color.WHITE
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        bluePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bluePaint.apply {
            strokeWidth = 5F
            color = ContextCompat.getColor(context, R.color.end_blue)
            isAntiAlias = true
            style = Paint.Style.STROKE
        }

        yellowPaint = Paint()
        yellowPaint.apply {
            strokeWidth = 5F
            color = ContextCompat.getColor(context, R.color.end_yellow)
            isAntiAlias = true
            style = Paint.Style.STROKE
        }

        greenPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        greenPaint.apply {
            strokeWidth = 5F
            color = ContextCompat.getColor(context, R.color.end_green)
            isAntiAlias = true
            style = Paint.Style.STROKE
        }

    }

    private fun createBoard(ludoMap: LudoMap,canvas: Canvas) {
        createNormalTiles(canvas)
        createRestingPlaces(canvas,ludoMap.restingPlaces)
    }

    private fun createRectangle(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        canvas: Canvas,
        paint: Paint
    ) {
        val rectF = RectF(column[x], row[y], column[x + width], row[y + height])
        canvas.drawRect(rectF, paint)
    }

    private fun createRoundRect(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        canvas: Canvas,
        startColor:Int,
        endColor:Int,
        radius: Float
    ) {

        val mShader: Shader = LinearGradient(
            x1,
            y1,
            x2,
            y2,
            ContextCompat.getColor(context, startColor), ContextCompat.getColor(context, endColor),
            TileMode.CLAMP
        )

        val paint = Paint()
        paint.apply {
            shader = mShader
        }

        val rectF = RectF(
            x1,
            y1,
            x2,
            y2
        )
        canvas.drawRoundRect(rectF, radius, radius, paint)
        canvas.drawRoundRect(rectF, radius, radius, borderPaint)
    }

    private fun createRoundRect(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        canvas: Canvas,
        color: PlayerColors,
        radius: Float
    ) {
        var startColor : Int = 0
        var endColor : Int=0
        when(color){
            PlayerColors.YELLOW->{
                startColor = R.color.start_yellow
                endColor = R.color.end_yellow
            }
            PlayerColors.GREEN->{
                startColor = R.color.start_green
                endColor = R.color.end_green
            }
            PlayerColors.RED->{
                startColor = R.color.start_red
                endColor = R.color.end_red
            }
            PlayerColors.BLUE->{
                startColor = R.color.start_blue
                endColor = R.color.end_blue
            }
            PlayerColors.WHITE->{
                startColor = R.color.start_white
                endColor = R.color.end_white
            }


        }
        val mShader: Shader = LinearGradient(
            column[x] + cellPadding,
            row[y] + cellPadding,
            column[x + width] - cellPadding,
            row[y + height] - cellPadding,
            ContextCompat.getColor(context, startColor), ContextCompat.getColor(context, endColor),
            TileMode.CLAMP
        )

        val paint = Paint()
        paint.apply {
            shader = mShader
        }

        val rectF = RectF(
            column[x] + cellPadding,
            row[y] + cellPadding,
            column[x + width] - cellPadding,
            row[y + height] - cellPadding
        )
        canvas.drawRoundRect(rectF, radius, radius, paint)
        canvas.drawRoundRect(rectF, radius, radius, borderPaint)
    }

    private fun drawDrawables(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        canvas: Canvas,
        img: Int,
        padding: Int
    ) {
        val mDrawable = ContextCompat.getDrawable(context, img)
        val dPadding = cellPadding * padding
        mDrawable!!.setBounds(
            (column[x] + dPadding).toInt(),
            (row[y] + dPadding).toInt(),
            (column[x + width] - dPadding).toInt(),
            (row[y + height] - dPadding).toInt()
        )

        mDrawable.draw(canvas)
    }

    private fun createNormalTiles(canvas: Canvas) {
        val greyStar = R.drawable.ic_grey_star
        val whiteStar = R.drawable.ic_white_star

        for(tile in ludoMap.tiles){
            createRoundRect(tile.column-1,tile.row-1,1,1,canvas,tile.color,tileRadius)
            if (tile.isStop){
                if (tile.color==PlayerColors.WHITE){
                    drawDrawables(
                        tile.column-1,tile.row-1, 1, 1,
                        canvas, greyStar, 3
                    )
                }else{
                    drawDrawables(
                        tile.column-1,tile.row-1, 1, 1,
                        canvas, whiteStar, 3
                    )
                }
            }

            when(tile.gateFor){
                PlayerColors.YELLOW->{
                    drawDrawables(
                        tile.column-1,tile.row-1, 1, 1,
                        canvas, R.drawable.ic_yellow_arrow, 4
                    )
                }
                PlayerColors.GREEN->{
                    drawDrawables(
                        tile.column-1,tile.row-1, 1, 1,
                        canvas, R.drawable.ic_green_arrow, 4
                    )
                }
                PlayerColors.RED->{
                    drawDrawables(
                        tile.column-1,tile.row-1, 1, 1,
                        canvas, R.drawable.ic_red_arrow, 4
                    )
                }
                PlayerColors.BLUE->{
                    drawDrawables(
                        tile.column-1,tile.row-1, 1, 1,
                        canvas, R.drawable.ic_blue_arrow, 4
                    )
                }
                else->{

                }
            }
        }

        for(paths in ludoMap.homePaths){
            for (tile in paths) {
                createRoundRect(tile.column - 1, tile.row - 1, 1, 1, canvas, tile.color, tileRadius)
            }
        }

        drawDrawables(6, 6, 3, 3, canvas, R.drawable.ic_ludo_center, 1)
    }

    private fun createRestingPlaces(canvas: Canvas, restingPlaces: List<RestingPlace>) {
        for (place in restingPlaces) {
            createRoundRect(place.pX,place.pY,6,6,canvas,place.color,restingPlaceRadius)
            createCircle(place.circularBase.pX,place.circularBase.pY,canvas,whitePaint,d*2)
            for (spot in place.spots){
                when(spot.color){
                    PlayerColors.YELLOW->canvas.drawCircle(spot.pX, spot.pY, spot.radius, yellowPaint)
                    PlayerColors.GREEN->canvas.drawCircle(spot.pX, spot.pY, spot.radius, greenPaint)
                    PlayerColors.RED->canvas.drawCircle(spot.pX, spot.pY, spot.radius, redPaint)
                    PlayerColors.BLUE->canvas.drawCircle(spot.pX, spot.pY, spot.radius, bluePaint)
                }

            }
        }

    }

    private fun createCircle(
        x: Int,
        y: Int,
        canvas: Canvas,
        paint: Paint,
        radius: Float
    ) {
        canvas.drawCircle(column[x], row[y], radius, paint)
    }


    private fun populateDisplayMatrix() {
        for (i in 0..15) column.add(d * i)
        for (j in 0..15) row.add(topSpacing + d * j)

//        for (i in 0..14){
//            val row = mutableListOf<Pair<Float,Float>>()
//            for (j in 0..14){
//                row.add(Pair(d*j,topSpacing+d*i))
//            }
//            _matrix.add(row)
//        }
    }

}