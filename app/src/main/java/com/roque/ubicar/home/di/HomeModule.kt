package com.roque.ubicar.home.di

import android.content.Context
import androidx.room.Room
import com.roque.ubicar.home.data.LocationServiceImpl
import com.roque.ubicar.home.data.local.UbicarDatabase
import com.roque.ubicar.home.data.local.dao.HomeDao
import com.roque.ubicar.home.data.repository.HomeRepositoryImpl
import com.roque.ubicar.home.domain.HomeRepository
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

    @Provides
    @Singleton
    fun provideHomeDao(@ApplicationContext context: Context): HomeDao =
        Room.databaseBuilder(context, UbicarDatabase::class.java, "ubicar_db").build().dao

    @Provides
    @Singleton
    fun provideHomeRepository(dao: HomeDao): HomeRepository = HomeRepositoryImpl(dao)

}