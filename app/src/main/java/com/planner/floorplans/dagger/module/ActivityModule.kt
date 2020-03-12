package com.planner.floorplans.dagger.module

import com.planner.floorplans.ui.MainActivity
import com.planner.floorplans.ui.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity
}
