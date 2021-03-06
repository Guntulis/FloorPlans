package com.planner.floorplans.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Ground(

    @SerializedName("materials")
    @Expose
    var materials: Materials? = null

) : FloorItem()