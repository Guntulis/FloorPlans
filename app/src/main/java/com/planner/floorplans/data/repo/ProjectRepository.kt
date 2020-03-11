package com.planner.floorplans.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.currencyapp.data.api.Resource
import com.planner.floorplans.data.api.ApiClient
import com.planner.floorplans.data.model.Project
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProjectRepository(val apiClient: ApiClient) {

    private val _projectState = MutableLiveData<Resource<Project>>()
    val project: LiveData<Resource<Project>>
        get() = _projectState

    private val _compositeDisposable = CompositeDisposable()

    fun loadProjectData(projectId: String?) {
        if (projectId == null) {
            _projectState.value = Resource.Error("Invalid project id")
        } else {
            _projectState.value = Resource.Loading()
            apiClient.getProject(projectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { venue ->
                        venue?.let {
                            _projectState.value = Resource.Complete(venue)
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