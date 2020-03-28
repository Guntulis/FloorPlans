package com.planner.floorplans.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Wall(

    @SerializedName("w")
    @Expose
    var width: Float? = null,

    @SerializedName("items")
    @Expose
    var points: List<WallPoint>? = null
)