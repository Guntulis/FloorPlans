package com.planner.floorplans.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class WallPoint(

    @SerializedName("x")
    @Expose
    var x: BigDecimal = BigDecimal.ZERO,

    @SerializedName("y")
    @Expose
    var y: BigDecimal = BigDecimal.ZERO
)