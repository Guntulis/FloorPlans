package com.example.currencyapp.dagger.module

import com.example.currencyapp.data.api.ApiClient
import com.example.currencyapp.data.state.CurrencyRatesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun providesCurrencyRatesRepository(api: ApiClient): CurrencyRatesRepository {
        return CurrencyRatesRepository(api)
    }
}