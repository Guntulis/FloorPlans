package com.planner.floorplans.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.planner.floorplans.R
import com.planner.floorplans.data.model.FloorItem
import com.planner.floorplans.data.model.Project
import com.planner.floorplans.data.model.Room
import java.math.BigDecimal

class FloorPlanView : View {
    private val framePaint = Paint()
    private var groundWidth: BigDecimal = BigDecimal.ZERO
    private var groundHeight: BigDecimal = BigDecimal.ZERO
    private val groundPaint = Paint()
    private val roomPaint = Paint()
    private val wallPaint = Paint()
    private var zoom = BigDecimal.ONE

    private var floorItems = listOf<FloorItem>()

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    init {
        framePaint.color = Color.RED
        framePaint.strokeWidth = 2f
        framePaint.style = Paint.Style.STROKE
        roomPaint.style = Paint.Style.FILL
        wallPaint.color = ContextCompat.getColor(context, R.color.wallColor)
        wallPaint.strokeCap = Paint.Cap.SQUARE
    }

    @Suppress("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (groundHeight == BigDecimal.ZERO || groundWidth == BigDecimal.ZERO) {
            return
        }

        canvas?.run {
            val portraitMode = width <= height
            val scaledBy = if (portraitMode) {
                width.toBigDecimal().divide(groundHeight)
            } else {
                height.toBigDecimal().divide(groundWidth)
            }
            val groundWidthScaled = groundWidth * scaledBy * zoom
            val groundHeightScaled = groundHeight * scaledBy * zoom
            val centerX = width.toBigDecimal() / BigDecimal(2)
            val centerY = height.toBigDecimal() / BigDecimal(2)
            val topLeftGroundX = centerX - groundWidthScaled / BigDecimal(2)
            val topLeftGroundY = centerY - groundHeightScaled / BigDecimal(2)
            drawRect(
                topLeftGroundX.toFloat(),
                topLeftGroundY.toFloat(),
                (topLeftGroundX + groundWidthScaled).toFloat(),
                (topLeftGroundY + groundHeightScaled).toFloat(),
                groundPaint
            )
            drawFloorItems(this, scaledBy, topLeftGroundX, topLeftGroundY)
        }
    }

    private fun drawFloorItems(
        canvas: Canvas,
        scaledBy: BigDecimal,
        topLeftGroundX: BigDecimal,
        topLeftGroundY: BigDecimal
    ) {
        floorItems.forEach { floorItem ->
            if (floorItem is Room) {
                val firstWallPoint = floorItem.walls?.first()?.points?.first()
                val firstWallPointX = (firstWallPoint?.x ?: BigDecimal.ZERO) * scaledBy * zoom
                val firstWallPointY = (firstWallPoint?.y ?: BigDecimal.ZERO) * scaledBy * zoom
                val floorX = (floorItem.x ?: BigDecimal.ZERO) * scaledBy * zoom
                val floorY = (floorItem.y ?: BigDecimal.ZERO) * scaledBy * zoom
                val topLeftRoomX = topLeftGroundX + floorX
                val topLeftRoomY = topLeftGroundY + floorY
                val path = Path()
                path.moveTo((topLeftRoomX + firstWallPointX).toFloat(), (topLeftRoomY + firstWallPointY).toFloat())
                floorItem.walls?.forEach { wall ->
                    wallPaint.strokeWidth = ((wall.width ?: BigDecimal.ZERO) * scaledBy * zoom).toFloat()
                    val wallPoints = wall.points
                    wallPoints?.let { wallPoint ->
                        val x0 = topLeftRoomX + (wallPoint[0].x ?: BigDecimal.ZERO) * scaledBy * zoom
                        val y0 = topLeftRoomY + (wallPoint[0].y ?: BigDecimal.ZERO) * scaledBy * zoom
                        val x1 = topLeftRoomX + (wallPoint[1].x ?: BigDecimal.ZERO) * scaledBy * zoom
                        val y1 = topLeftRoomY + (wallPoint[1].y ?: BigDecimal.ZERO) * scaledBy * zoom
                        path.lineTo(x1.toFloat(), y1.toFloat())
                        canvas.drawLine(x0.toFloat(), y0.toFloat(), x1.toFloat(), y1.toFloat(), wallPaint)
                    }
                }
                roomPaint.color = tryParseColor(floorItem.materials?.floor?.color) ?: Color.WHITE
                canvas.drawPath(path, roomPaint)
            }
        }
    }

    private fun setGroundDimensions(width: BigDecimal, height: BigDecimal) {
        groundWidth = width
        groundHeight = height
    }

    private fun setGroundColor(barBackgroundColor: Int) {
        groundPaint.color = barBackgroundColor
    }

    fun setProject(project: Project?, zoom: Float) {
        this.zoom = zoom.toBigDecimal()
        val groundColor = tryParseColor(project?.ground?.color)
        setGroundColor(groundColor ?: Color.WHITE)
        setGroundDimensions(project?.width ?: BigDecimal.ZERO, project?.height ?: BigDecimal.ZERO)
        val firstFloor = project?.floors?.first()
        floorItems = firstFloor?.floorItems ?: listOf()
        invalidate()
    }

    fun setScaleFactor(zoom: Float) {
        this.zoom = zoom.toBigDecimal()
        invalidate()
    }

    private fun tryParseColor(color: String?): Int? {
        return color?.let { value ->
            return try {
                Color.parseColor(value)
            } catch (e: IllegalArgumentException) {
                Log.e(TAG, "Failed to parse color $color", e)
                null
            } catch (e: StringIndexOutOfBoundsException) {
                Log.e(TAG, "Failed to parse color $color", e)
                null
            }
        }
    }

    companion object {
        val TAG: String = FloorPlanView::class.java.simpleName
    }
}