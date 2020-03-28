package com.planner.floorplans.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Ns (

    @SerializedName("materials")
    @Expose
    var materials: List<Materials>? = null

) : FloorItem()