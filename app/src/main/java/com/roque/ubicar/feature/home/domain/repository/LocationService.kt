package com.roque.ubicar.feature.home.domain.repository

import com.roque.ubicar.feature.home.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationService {
    fun getLocationUpdates(): Flow<Location?>
    fun stopLocationUpdates()
    suspend fun getCurrentLocation(): Location?
}
