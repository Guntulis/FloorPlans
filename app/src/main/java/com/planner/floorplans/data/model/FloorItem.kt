package com.planner.floorplans.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

 abstract class FloorItem (

    @SerializedName("className")
    @Expose
    var className: String? = null,

    @SerializedName("x")
    @Expose
    var x: Float? = null,

    @SerializedName("y")
    @Expose
    var y: Float? = null,

    @SerializedName("sX")
    @Expose
    var sX: Float? = null,

    @SerializedName("sY")
    @Expose
    var sY: Float? = null,

    @SerializedName("items")
    @Expose
    var walls: List<Wall>? = null
)