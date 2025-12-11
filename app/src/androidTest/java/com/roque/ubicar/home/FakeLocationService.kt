package com.roque.ubicar.home

import com.roque.ubicar.home.domain.LocationService
import com.roque.ubicar.home.domain.model.Location
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocationService : LocationService {

    val locations = listOf(
        Location(10.0, 10.0),
        Location(15.0, 15.0),
        Location(20.0, 20.0)
    )

    var shouldTrack = false

    override fun getLocationUpdates(): Flow<Location?> = flow {
        var count = 0
        shouldTrack = true
        while (shouldTrack) {
            emit(locations[count % locations.size])
            count++
            delay(1000)
        }
    }

    override fun stopLocationUpdates() {
        shouldTrack = false
    }

    override suspend fun getCurrentLocation(): Location? = locations.first()

}