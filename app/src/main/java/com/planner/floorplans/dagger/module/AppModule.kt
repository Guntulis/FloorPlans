package com.planner.floorplans.dagger.module

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.planner.floorplans.BuildConfig
import com.planner.floorplans.data.api.ApiClient
import com.planner.floorplans.data.api.RuntimeTypeAdapterFactory
import com.planner.floorplans.data.model.*
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

    @Singleton
    @Provides
    fun provideGson(): Gson {
        val runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
            .of(FloorItem::class.java, "className")
            .registerSubtype(Room::class.java, "Room")
            .registerSubtype(Ground::class.java, "Ground")
            .registerSubtype(Pr::class.java, "Pr")
            .registerSubtype(Ns::class.java, "Ns")
            .registerSubtype(Door::class.java, "Door")
            .registerSubtype(Window::class.java, "Window")
            .registerSubtype(Ruler::class.java, "Ruler")

        return GsonBuilder()
            .registerTypeAdapterFactory(runtimeTypeAdapterFactory)
            .create()
    }

    @Provides
    @Singleton
    fun providesRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }
}