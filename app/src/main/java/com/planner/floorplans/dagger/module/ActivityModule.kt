package com.example.currencyapp.dagger.module

import com.example.currencyapp.ui.MainActivity
import com.example.currencyapp.ui.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity
}
