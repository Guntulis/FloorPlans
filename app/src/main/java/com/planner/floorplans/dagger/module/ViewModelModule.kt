package com.planner.floorplans.dagger.module

import androidx.lifecycle.ViewModel
import com.planner.floorplans.dagger.annotation.ViewModelKey
import com.planner.floorplans.data.repo.ProjectRepository
import com.planner.floorplans.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Module
    companion object {

        @Provides
        @Singleton
        @JvmStatic
        fun provideMainViewModel(projectRepository: ProjectRepository): MainViewModel {
            return MainViewModel(projectRepository)
        }
    }
}