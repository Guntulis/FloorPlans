package com.planner.floorplans.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProjectResponseItems(

    @SerializedName("name")
    @Expose
    var name: String? = null,


    @SerializedName("data")
    @Expose
    var data: Project? = null
)