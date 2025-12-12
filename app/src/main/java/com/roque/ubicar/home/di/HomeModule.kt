package com.roque.ubicar.home.di

import android.content.Context
import androidx.room.Room
import com.roque.ubicar.BuildConfig
import com.roque.ubicar.home.data.distance.DistanceCalculatorImpl
import com.roque.ubicar.home.data.local.UbicarDatabase
import com.roque.ubicar.home.data.local.dao.HomeDao
import com.roque.ubicar.home.data.remote.ApiClient
import com.roque.ubicar.home.data.remote.GoogleDirectionsApi
import com.roque.ubicar.home.data.repository.HomeRepositoryImpl
import com.roque.ubicar.home.data.repository.LocationServiceImpl
import com.roque.ubicar.home.domain.HomeRepository
import com.roque.ubicar.home.domain.LocationService
import com.roque.ubicar.home.domain.distance.DistanceCalculator
import com.roque.ubicar.home.domain.usecase.GetPathToCarUseCase
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
    fun provideClient(): Retrofit = ApiClient.create(
        baseUrl = GoogleDirectionsApi.BASE_URL,
        apiKey = BuildConfig.GOOGLE_MAPS_API_KEY
    )

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