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

    init {
        projectRepository.loadProjectIds()
    }

    private val _visibleProjectIndex = MutableLiveData<Int>()
    private val _nextProjectIndex = MutableLiveData<Int>()

    private val _visibleProject: MergeLiveData<Resource<List<String>>, Int, Resource<Project>> =
        MergeLiveData(projectRepository.projectIdList, _visibleProjectIndex) { projectIds, index ->
            _visibleProjectIndex.value?.let { projectIndex->
                when (projectIds) {
                    is Loading -> {
                        Loading<Project>()
                    }
                    is Empty -> {
                        Empty<Project>()
                    }
                    is Complete -> {
                        if (projectIndex < projectIds.value.size) {
                            val projectId = projectIds.value[projectIndex]
                            Log.d(TAG, "Loading project $projectId")
                            projectRepository.loadProjectData(projectId)
                            Loading<Project>()
                        } else {
                            Error<Project>("Index out of bounds")
                        }
                    }
                    else -> {
                        Error<Project>("Failed to load project list")
                    }
                }
            } ?: Loading<Project>()
        }
    val visibleProject: LiveData<Resource<Project>>
        get() = _visibleProject


    companion object {
        val TAG: String = MainViewModel::class.java.simpleName
    }
}
