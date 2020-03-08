package com.example.currencyapp.dagger.module

import android.app.Application
import android.content.Context
import com.planner.floorplans.BuildConfig
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    fun provideContext(): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }

    @Provides
    @Singleton
    fun providesAppPreferences(context: Context): AppPreferences {
        return AppPreferences(context)
    }

    @Provides
    @Singleton
    fun provideCurrencyRatesAdapter(context: Context): CurrencyRatesAdapter {
        return CurrencyRatesAdapter(context)
    }

    @Provides
    @Singleton
    fun provideTimer(): Timer {
        return Timer()
    }
}