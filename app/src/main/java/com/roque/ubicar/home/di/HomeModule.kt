package com.roque.ubicar.home.di

import android.content.Context
import androidx.room.Room
import com.roque.ubicar.home.data.repository.LocationServiceImpl
import com.roque.ubicar.home.data.local.UbicarDatabase
import com.roque.ubicar.home.data.local.dao.HomeDao
import com.roque.ubicar.home.data.remote.GoogleDirectionsApi
import com.roque.ubicar.home.data.repository.HomeRepositoryImpl
import com.roque.ubicar.home.domain.HomeRepository
import com.roque.ubicar.home.domain.LocationService
import com.roque.ubicar.home.domain.usecase.GetPathToCarUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
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
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideDirectionsApi(client: OkHttpClient): GoogleDirectionsApi {
        return Retrofit.Builder()
            .baseUrl(GoogleDirectionsApi.BASE_URL)
            .client(client)
            .addConverterFactory(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }.asConverterFactory("application/json".toMediaType())
            )
            .build().create()
    }

    @Provides
    @Singleton
    fun provideHomeRepository(
        dao: HomeDao,
        api: GoogleDirectionsApi
    ): HomeRepository = HomeRepositoryImpl(dao, api)

    @Provides
    @Singleton
    fun provideGetPathToCarUseCase(repository: HomeRepository): GetPathToCarUseCase =
        GetPathToCarUseCase(repository)

}