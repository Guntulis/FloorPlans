package com.planner.floorplans.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Wall(

    @SerializedName("w")
    @Expose
    var width: BigDecimal = BigDecimal.ZERO,

    @SerializedName("items")
    @Expose
    var points: List<WallPoint>? = null
)