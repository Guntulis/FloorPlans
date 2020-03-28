package com.planner.floorplans.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Materials(

    @SerializedName("floor")
    @Expose
    var floor: Material? = null,

    @SerializedName("ceil")
    @Expose
    var ceil: Material? = null
)