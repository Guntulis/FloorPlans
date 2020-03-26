package com.planner.floorplans.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Wall(

    @SerializedName("items")
    @Expose
    var points: List<WallPoint>? = null
)