package com.roque.ubicar.home.domain

import com.roque.ubicar.home.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationService {
    fun getLocationUpdates(): Flow<Location?>
    fun stopLocationUpdates()
    suspend fun getCurrentLocation(): Location?
}
