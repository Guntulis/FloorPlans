package com.planner.floorplans.dagger.module

import com.planner.floorplans.data.api.ApiClient
import com.planner.floorplans.data.repo.ProjectRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun providesProjectRepository(api: ApiClient): ProjectRepository {
        return ProjectRepository(api)
    }
}