package com.planner.floorplans.ui.main

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

    private val _currentProjectIndex = MutableLiveData<Int>()
    val currentProjectIndex: LiveData<Int>
        get() = _currentProjectIndex

    private val _currentProject: MergeLiveData<Resource<List<String>>, Int, Resource<Project>> =
        MergeLiveData(projectRepository.projectIdList, currentProjectIndex) { projectIds, index ->
            when (projectIds) {
                is Loading -> {
                    Loading()
                }
                is Empty -> {
                    Empty<Project>()
                }
                is Complete -> {
                    projectRepository.loadProjectData(projectIds.value[index!!])
                    Loading<Project>()
                }
                else -> {
                    Error("Failed to load menu")
                }
            }
        }

    val currentProject: LiveData<Resource<Project>>
        get() = projectRepository.project

    fun loadProjectIds() {
        projectRepository.loadProjectIds()
    }
}
