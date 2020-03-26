package com.planner.floorplans.data.api

import com.planner.floorplans.data.model.ProjectResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {

    @GET("/gallery/floorplans")
    fun getFloorPlansHtml(): Observable<Response<ResponseBody>>

    @GET("/api/project/{projectId}")
    fun getProject(
        @Path("projectId") projectId: String
    ): Observable<ProjectResponse>
}