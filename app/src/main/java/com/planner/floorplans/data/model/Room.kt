package com.planner.floorplans.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Room(

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