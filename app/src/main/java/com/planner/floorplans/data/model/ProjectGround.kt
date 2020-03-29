package com.planner.floorplans.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProjectGround(

    @SerializedName("color")
    @Expose
    var color: String? = null

) : FloorItem()