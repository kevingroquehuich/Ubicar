package com.roque.ubicar.feature.home.di

import android.content.Context
import androidx.room.Room
import com.roque.ubicar.feature.home.data.distance.DistanceCalculatorImpl
import com.roque.ubicar.feature.home.data.local.UbicarDatabase
import com.roque.ubicar.feature.home.data.local.dao.HomeDao
import com.roque.ubicar.feature.home.data.remote.GoogleDirectionsApi
import com.roque.ubicar.feature.home.data.repository.HomeRepositoryImpl
import com.roque.ubicar.feature.home.data.repository.LocationServiceImpl
import com.roque.ubicar.feature.home.domain.distance.DistanceCalculator
import com.roque.ubicar.feature.home.domain.repository.HomeRepository
import com.roque.ubicar.feature.home.domain.repository.LocationService
import com.roque.ubicar.feature.home.domain.usecase.GetPathToCarUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
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
    fun provideDirectionsApi(retrofit: Retrofit): GoogleDirectionsApi = retrofit.create()

    @Provides
    @Singleton
    fun provideHomeRepository(
        dao: HomeDao,
        api: GoogleDirectionsApi
    ): HomeRepository = HomeRepositoryImpl(dao, api)

    @Provides
    @Singleton
    fun provideDistanceCalculator(): DistanceCalculator = DistanceCalculatorImpl()

    @Provides
    @Singleton
    fun provideGetPathToCarUseCase(
        repository: HomeRepository,
        distanceCalculator: DistanceCalculator
    ): GetPathToCarUseCase =
        GetPathToCarUseCase(repository, distanceCalculator)

}