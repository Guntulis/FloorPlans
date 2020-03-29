package com.planner.floorplans.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Project(

    @SerializedName("version")
    @Expose
    var version: Int? = null,

    @SerializedName("width")
    @Expose
    var width: BigDecimal? = null,

    @SerializedName("height")
    @Expose
    var height: BigDecimal? = null,

    @SerializedName("currentFloor")
    @Expose
    var currentFloor: String? = null,

    @SerializedName("ground")
    @Expose
    var ground: ProjectGround? = null,

    @SerializedName("items")
    @Expose
    var floors: List<Floor>? = null

)