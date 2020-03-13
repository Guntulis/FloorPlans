package com.planner.floorplans.ui.main

import androidx.lifecycle.ViewModel
import com.planner.floorplans.data.repo.ProjectRepository

class MainViewModel(private val projectRepository: ProjectRepository) : ViewModel() {
    fun loadData() {
        projectRepository.loadProjectIDList()
    }
}
