package com.planner.floorplans.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Floor(

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("items")
    @Expose
    var rooms: List<Room>? = null
)