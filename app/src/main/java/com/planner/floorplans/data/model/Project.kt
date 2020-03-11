package com.planner.floorplans.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Project (
    @SerializedName("storage")
    @Expose
    var storage: String? = null
)