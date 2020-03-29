package com.planner.floorplans.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.planner.floorplans.data.api.ApiClient
import com.planner.floorplans.data.api.Resource
import com.planner.floorplans.data.api.Resource.Complete
import com.planner.floorplans.data.api.Resource.Empty
import com.planner.floorplans.data.api.Resource.Error
import com.planner.floorplans.data.api.Resource.Loading
import com.planner.floorplans.data.model.ProjectResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProjectRepository(private val apiClient: ApiClient) {

    private val _projectIdListState = MutableLiveData<Resource<List<String>>>()
    val projectIdList: LiveData<Resource<List<String>>>
        get() = _projectIdListState

    private val _visibleProjectState = MutableLiveData<Resource<ProjectResponse>>()
    val visibleProject: LiveData<Resource<ProjectResponse>>
        get() = _visibleProjectState

    private val _nextProjectState = MutableLiveData<Resource<ProjectResponse>>()
    val nextProject: LiveData<Resource<ProjectResponse>>
        get() = _nextProjectState

    private val _compositeDisposable = CompositeDisposable()

    fun loadProjectIds() {
        _projectIdListState.value = Loading()
        apiClient.getFloorPlansHtml()
            .subscribeOn(Schedulers.io())
            .map { responseBody -> findProjectIds(responseBody.body()?.string()) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { projectIds ->
                    Log.d(TAG, "Loaded project ids: $projectIds")
                    projectIds?.let {
                        _projectIdListState.value = Complete(projectIds)
                    } ?: run {
                        _projectIdListState.value = Empty()
                    }
                },
                { error ->
                    _projectIdListState.value = Error("Failed to load floor plans")
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

    fun loadVisibleProject(projectIndex: Int) {
        when (val idListState = _projectIdListState.value) {
            is Complete -> {
                _visibleProjectState.value = Loading()
                apiClient.getProject(idListState.value[projectIndex])
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { project ->
                            project?.let {
                                _visibleProjectState.value = Complete(project)
                            }
                        },
                        { error ->
                            _visibleProjectState.value = Error("Failed to load project")
                            Log.e(TAG, "Failed to load project", error)
                        }
                    ).also { _compositeDisposable.add(it) }
            }
            else -> {
                _visibleProjectState.value = Error("Invalid project id list state")
            }
        }
    }

    fun loadNextProjectData(projectIndex: Int) {
        when (val idListState = _projectIdListState.value) {
            is Complete -> {
                _nextProjectState.value = Loading()
                if (idListState.value.size > projectIndex) {
                    apiClient.getProject(idListState.value[projectIndex])
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { project ->
                                project?.let {
                                    _nextProjectState.value = Complete(project)
                                }
                            },
                            { error ->
                                _nextProjectState.value = Error("Failed to load project")
                                Log.e(TAG, "Failed to load project", error)
                            }
                        ).also { _compositeDisposable.add(it) }
                } else {
                    _nextProjectState.value = Empty()
                }
            }
            else -> {
                _nextProjectState.value = Error("Invalid project id list state")
            }
        }
    }

    fun swapVisibleWithNext() {
        val nextProject = _nextProjectState.value
        _visibleProjectState.value = nextProject
    }

    companion object {
        val TAG: String = ProjectRepository::class.java.simpleName
    }
}