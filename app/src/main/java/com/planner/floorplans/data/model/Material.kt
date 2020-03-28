package com.planner.floorplans.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Material(

    @SerializedName("texture")
    @Expose
    var texture: String? = null,

    @SerializedName("color")
    @Expose
    var color: String? = null
)