package com.planner.floorplans.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class FloorPlanView : View {
    private val framePaint = Paint()
    private var groundWidth: Float = 0f
    private var groundHeight: Float = 0f
    private val groundPaint = Paint()

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    init {
        framePaint.color = Color.RED
        framePaint.strokeWidth = 2f
        framePaint.style = Paint.Style.STROKE
        //groundPaint.isAntiAlias = true
    }

    @Suppress("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.run {
            drawRect(0f, 0f, width.toFloat(), height.toFloat(), framePaint)
            val scaledBy = min(width.toFloat(), height.toFloat()) / groundHeight
            val groundHeightScaled = groundHeight * scaledBy
            val topLeftGroundY = (height - groundHeightScaled) / 2
            drawRect(0f, topLeftGroundY, width.toFloat(), topLeftGroundY + groundHeightScaled, groundPaint)
        }
    }

    fun setGroundDimensions(width: Float, height: Float) {
        groundWidth = width
        groundHeight = height
    }

    fun setGroundColor(barBackgroundColor: Int) {
        groundPaint.color = barBackgroundColor
    }
}