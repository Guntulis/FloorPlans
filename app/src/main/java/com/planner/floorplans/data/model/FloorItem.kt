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
    var x: BigDecimal? = null,

    @SerializedName("y")
    @Expose
    var y: BigDecimal? = null,

    @SerializedName("sX")
    @Expose
    var sX: Double? = null,

    @SerializedName("sY")
    @Expose
    var sY: Double? = null,

    @SerializedName("items")
    @Expose
    var walls: List<Wall>? = null
)