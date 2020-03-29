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
    private var zoom = 1

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
            val groundWidthScaled = groundWidth * scaledBy
            val groundHeightScaled = groundHeight * scaledBy
            val topLeftGroundX = if (portraitMode) {
                BigDecimal.ZERO
            } else {
                (width.toBigDecimal() - groundWidthScaled) / BigDecimal(2)
            }
            val topLeftGroundY = if (portraitMode) {
                (height.toBigDecimal() - groundHeightScaled) / BigDecimal(2)
            } else {
                BigDecimal.ZERO
            }
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
                val firstWallPointX = (firstWallPoint?.x ?: BigDecimal.ZERO) * scaledBy
                val firstWallPointY = (firstWallPoint?.y ?: BigDecimal.ZERO) * scaledBy
                val floorX = (floorItem.x ?: BigDecimal.ZERO) * scaledBy
                val floorY = (floorItem.y ?: BigDecimal.ZERO) * scaledBy
                val topLeftRoomX = topLeftGroundX + floorX
                val topLeftRoomY = topLeftGroundY + floorY
                val path = Path()
                path.moveTo((topLeftRoomX + firstWallPointX).toFloat(), (topLeftRoomY + firstWallPointY).toFloat())
                floorItem.walls?.forEach { wall ->
                    wallPaint.strokeWidth = ((wall.width ?: BigDecimal.ZERO) * scaledBy).toFloat()
                    val wallPoints = wall.points
                    wallPoints?.let { wallPoint ->
                        val x0 = topLeftRoomX + (wallPoint[0].x ?: BigDecimal.ZERO) * scaledBy
                        val y0 = topLeftRoomY + (wallPoint[0].y ?: BigDecimal.ZERO) * scaledBy
                        val x1 = topLeftRoomX + (wallPoint[1].x ?: BigDecimal.ZERO) * scaledBy
                        val y1 = topLeftRoomY + (wallPoint[1].y ?: BigDecimal.ZERO) * scaledBy
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

    fun setProject(project: Project?, zoom: Int) {
        this.zoom = zoom
        val groundColor = tryParseColor(project?.ground?.color)
        setGroundColor(groundColor ?: Color.WHITE)
        setGroundDimensions(project?.width ?: BigDecimal.ZERO, project?.height ?: BigDecimal.ZERO)
        val firstFloor = project?.floors?.first()
        floorItems = firstFloor?.floorItems ?: listOf()
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