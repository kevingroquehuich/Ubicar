package com.roque.ubicar.core.di

import com.roque.ubicar.BuildConfig
import com.roque.ubicar.core.data.remote.ApiClient
import com.roque.ubicar.feature.home.data.remote.GoogleDirectionsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideClient(): Retrofit = ApiClient.create(
        baseUrl = GoogleDirectionsApi.BASE_URL,
        apiKey = BuildConfig.GOOGLE_MAPS_API_KEY
    )

}