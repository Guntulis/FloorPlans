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
import kotlin.math.min

class FloorPlanView : View {
    private val framePaint = Paint()
    private var groundWidth: Float = 0f
    private var groundHeight: Float = 0f
    private val groundPaint = Paint()
    private val roomPaint = Paint()
    private val wallPaint = Paint()

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

        canvas?.run {
            //drawRect(0f, 0f, width.toFloat(), height.toFloat(), framePaint)
            val scaledBy = min(width.toFloat(), height.toFloat()) / groundHeight
            val groundHeightScaled = groundHeight * scaledBy
            val topLeftGroundY = (height - groundHeightScaled) / 2
            drawRect(0f, topLeftGroundY, width.toFloat(), topLeftGroundY + groundHeightScaled, groundPaint)
            floorItems.forEach { floorItem ->
                if (floorItem is Room) {
                    val topLeftRoomX = (floorItem.x ?: 0f) * scaledBy
                    val topLeftRoomY = topLeftGroundY + ((floorItem.y ?: 0f) * scaledBy)
                    /*val bottomRightRoomX = topLeftRoomX + ((floorItem.sX ?: 0f) * scaledBy)
                    val bottomRightRoomY = topLeftRoomY + ((floorItem.sY ?: 0f) * scaledBy)*/
                    val path = Path()
                    path.moveTo(topLeftRoomX, topLeftRoomY)
                    floorItem.walls?.forEach { wall ->
                        wallPaint.strokeWidth = (wall.width ?: 0f) * scaledBy
                        val wallPoints = wall.points
                        wallPoints?.let {wallPoint ->
                            val x0 = topLeftRoomX + (wallPoint[0].x ?: 0f) * scaledBy
                            val y0 = topLeftRoomY + (wallPoint[0].y ?: 0f) * scaledBy
                            val x1 = topLeftRoomX + (wallPoint[1].x ?: 0f) * scaledBy
                            val y1 = topLeftRoomY + (wallPoint[1].y ?: 0f) * scaledBy
                            path.lineTo(x1, y1)
                            drawLine(x0, y0, x1, y1, wallPaint)
                        }
                    }
                    roomPaint.color = tryParseColor(floorItem.materials?.floor?.color) ?: Color.WHITE
                    drawPath(path, roomPaint)
                }
            }
        }
    }

    private fun setGroundDimensions(width: Float, height: Float) {
        groundWidth = width
        groundHeight = height
    }

    private fun setGroundColor(barBackgroundColor: Int) {
        groundPaint.color = barBackgroundColor
    }

    fun setProject(project: Project?) {
        val groundColor = tryParseColor(project?.ground?.color)
        setGroundColor(groundColor ?: Color.WHITE)
        setGroundDimensions(project?.width ?: 0f, project?.height ?: 0f)
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