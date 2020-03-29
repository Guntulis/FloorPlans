package com.planner.floorplans.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

abstract class FloorItem (

    @SerializedName("className")
    @Expose
    var className: String? = null,

    @SerializedName("x")
    @Expose
    var x: BigDecimal = BigDecimal.ZERO,

    @SerializedName("y")
    @Expose
    var y: BigDecimal = BigDecimal.ZERO,

    @SerializedName("items")
    @Expose
    var walls: List<Wall>? = null
)