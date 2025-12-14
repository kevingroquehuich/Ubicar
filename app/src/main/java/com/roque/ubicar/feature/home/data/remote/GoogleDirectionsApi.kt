package com.roque.ubicar.feature.home.data.remote

import com.roque.ubicar.feature.home.data.remote.dto.DirectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleDirectionsApi {
    companion object {
        const val BASE_URL = "https://maps.googleapis.com/"
    }

    @GET("maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String
    ): DirectionsResponse
}