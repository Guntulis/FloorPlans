package com.planner.floorplans.data.api

import com.planner.floorplans.data.model.Project
import io.reactivex.Observable
import retrofit2.http.*

interface ApiClient {

    @GET("/api/project/{projectId}")
    fun getProject(
        @Path("projectId") projectId: String
    ): Observable<Project>
}