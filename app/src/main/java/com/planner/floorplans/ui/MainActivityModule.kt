package com.planner.floorplans.ui

import com.planner.floorplans.ui.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector()
    abstract fun mainFragment(): MainFragment
}