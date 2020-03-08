package com.example.currencyapp.dagger.module

import androidx.lifecycle.ViewModel
import com.example.currencyapp.dagger.annotation.ViewModelKey
import com.example.currencyapp.data.AppPreferences
import com.example.currencyapp.data.state.CurrencyRatesRepository
import com.example.currencyapp.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideMenuViewModel(
            currencyRatesRepository: CurrencyRatesRepository,
            appPreferences: AppPreferences
        ): MainViewModel {
            return MainViewModel(currencyRatesRepository, appPreferences)
        }
    }
}