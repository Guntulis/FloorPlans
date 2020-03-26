package com.planner.floorplans.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProjectResponse(

    @SerializedName("storage")
    @Expose
    var storage: String? = null,

    @SerializedName("items")
    @Expose
    var items: List<ProjectResponseItems>? = null
)