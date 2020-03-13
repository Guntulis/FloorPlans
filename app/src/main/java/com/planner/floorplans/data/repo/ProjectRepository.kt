package com.planner.floorplans.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.planner.floorplans.data.api.Resource
import com.planner.floorplans.data.api.ApiClient
import com.planner.floorplans.data.model.Project
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProjectRepository(val apiClient: ApiClient) {

    private val _projectIdListState = MutableLiveData<Resource<List<String>>>()
    val projectIdList: LiveData<Resource<List<String>>>
        get() = _projectIdListState

    private val _projectState = MutableLiveData<Resource<Project>>()
    val project: LiveData<Resource<Project>>
        get() = _projectState

    private val _compositeDisposable = CompositeDisposable()

    fun loadProjectIDList() {
        _projectIdListState.value = Resource.Loading()
        apiClient.getFloorPlansHtml()
            .subscribeOn(Schedulers.io())
            .map { responseBody -> findProjectIds(responseBody.body()?.string()) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { projectIds ->
                    projectIds?.let {
                        _projectIdListState.value = Resource.Complete(projectIds)
                    } ?: run {
                        _projectIdListState.value = Resource.Empty()
                    }
                },
                { error ->
                    _projectIdListState.value = Resource.Error("Failed to load floor plans")
                    Log.e(TAG, "Failed to load floor plans", error)
                }
            ).also { _compositeDisposable.add(it) }
    }

    private fun findProjectIds(html: String?): List<String>? {
        return html?.let {
            val regex = Regex("https://planner5d.com/storage/thumbs.600/(.*?)[.jpg]")
            val matches = regex.findAll(html)
            matches.map { it.groupValues[1] }.toList()
        }
    }

    fun loadProjectData(projectId: String?) {
        if (projectId == null) {
            _projectState.value = Resource.Error("Invalid project id")
        } else {
            _projectState.value = Resource.Loading()
            apiClient.getProject(projectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { project ->
                        project?.let {
                            _projectState.value = Resource.Complete(project)
                        }
                    },
                    { error ->
                        _projectState.value = Resource.Error("Failed to load project")
                        Log.e(TAG, "Failed to load project", error)
                    }
                ).also { _compositeDisposable.add(it) }
        }
    }

    companion object {
        val TAG: String = ProjectRepository::class.java.simpleName
    }
}