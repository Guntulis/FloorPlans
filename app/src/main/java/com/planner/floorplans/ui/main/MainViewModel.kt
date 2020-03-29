package com.planner.floorplans.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.planner.floorplans.data.api.Resource
import com.planner.floorplans.data.model.ProjectResponse
import com.planner.floorplans.data.repo.ProjectRepository

class MainViewModel(private val projectRepository: ProjectRepository) : ViewModel() {

    private var visibleProjectIndex = 0
    private var nextProjectIndex = 1
    var scaleFactor = DEFAULT_SCALE_FACTOR

    val projectIdList
        get() = projectRepository.projectIdList

    val visibleProject: LiveData<Resource<ProjectResponse>>
        get() = projectRepository.visibleProject

    val nextProject: LiveData<Resource<ProjectResponse>>
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

    fun displayNextProject() {
        visibleProjectIndex++
        nextProjectIndex++
        projectRepository.swapVisibleWithNext()
    }

    fun startAgain() {
        visibleProjectIndex = 0
        nextProjectIndex = 1
        projectRepository.loadVisibleProjectData(visibleProjectIndex)
        projectRepository.loadNextProjectData(nextProjectIndex)
    }

    companion object {
        const val DEFAULT_SCALE_FACTOR = 1f
    }
}
