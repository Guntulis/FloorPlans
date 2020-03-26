package com.planner.floorplans.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.planner.floorplans.data.api.Resource
import com.planner.floorplans.data.api.Resource.Loading
import com.planner.floorplans.data.api.Resource.Empty
import com.planner.floorplans.data.api.Resource.Complete
import com.planner.floorplans.data.api.Resource.Error
import com.planner.floorplans.data.model.Project
import com.planner.floorplans.data.repo.ProjectRepository
import com.planner.floorplans.util.MergeLiveData

class MainViewModel(private val projectRepository: ProjectRepository) : ViewModel() {

    private var visibleProjectIndex = 0
    private var nextProjectIndex = 1

    val projectIdList
        get() = projectRepository.projectIdList

    val visibleProject: LiveData<Resource<Project>>
        get() = projectRepository.visibleProject

    val nextProject: LiveData<Resource<Project>>
        get() = projectRepository.nextProject

    init {
        projectRepository.loadProjectIds()
    }

    fun loadVisibleProject() {
        projectRepository.loadVisibleProjectData(visibleProjectIndex)
    }

    fun loadNextProject() {
        projectRepository.loadNextProjectData(nextProjectIndex)
    }

    companion object {
        val TAG: String = MainViewModel::class.java.simpleName
    }
}
