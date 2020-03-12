package com.planner.floorplans.dagger.component

import com.planner.floorplans.FloorPlansApplication
import com.planner.floorplans.dagger.module.ActivityModule
import com.planner.floorplans.dagger.module.AppModule
import com.planner.floorplans.dagger.module.RepositoryModule
import com.planner.floorplans.dagger.module.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ActivityModule::class,
        RepositoryModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {
    fun inject(application: FloorPlansApplication)
}