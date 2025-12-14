package com.roque.ubicar.feature.authentication.di

import android.content.Context
import com.roque.ubicar.feature.authentication.data.FirebaseAuthenticationRepository
import com.roque.ubicar.feature.authentication.domain.AuthenticationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Provides
    @Singleton
    fun proviesAuthenticationRepository(@ApplicationContext context: Context): AuthenticationRepository =
        FirebaseAuthenticationRepository(context)
}
