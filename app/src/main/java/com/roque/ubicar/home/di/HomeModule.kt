package com.roque.ubicar.home.di

import android.content.Context
import com.roque.ubicar.home.data.LocationServiceImpl
import com.roque.ubicar.home.domain.LocationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun provideLocationService(@ApplicationContext context: Context): LocationService =
        LocationServiceImpl(context)

}