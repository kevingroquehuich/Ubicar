package com.roque.ubicar.core.data.remote

import com.roque.ubicar.BuildConfig
import com.roque.ubicar.core.data.remote.interceptor.ApiKeyInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val MAX_TIMEOUT = 30

    fun create(
        baseUrl: String,
        apiKey: String
    ): Retrofit {
        val builder = OkHttpClient.Builder()

        builder.connectTimeout(MAX_TIMEOUT.toLong(), TimeUnit.SECONDS)
        builder.readTimeout(MAX_TIMEOUT.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(MAX_TIMEOUT.toLong(), TimeUnit.SECONDS)

        builder.addInterceptor(interceptor())
        builder.addInterceptor(ApiKeyInterceptor(apiKey))

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(builder.build())
            .addConverterFactory(
                jsonConverter.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    private val jsonConverter = Json {
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private fun interceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor   = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return httpLoggingInterceptor
    }

}