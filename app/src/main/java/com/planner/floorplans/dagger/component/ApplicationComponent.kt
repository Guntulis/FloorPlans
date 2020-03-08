package com.example.currencyapp.dagger.component

import com.example.currencyapp.CurrencyRatesApplication
import com.example.currencyapp.dagger.module.ActivityModule
import com.example.currencyapp.dagger.module.AppModule
import com.example.currencyapp.dagger.module.RepositoryModule
import com.example.currencyapp.dagger.module.ViewModelModule
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
    fun inject(application: CurrencyRatesApplication)
}