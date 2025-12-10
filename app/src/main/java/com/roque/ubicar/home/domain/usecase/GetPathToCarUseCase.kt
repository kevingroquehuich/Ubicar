package com.roque.ubicar.home.domain.usecase

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.utils.isLocationOnPath
import com.roque.ubicar.home.domain.HomeRepository
import com.roque.ubicar.home.domain.model.Location
import com.roque.ubicar.home.domain.model.Route

class GetPathToCarUseCase(
    private val repository: HomeRepository
) {

    companion object {
        const val MAX_METERS = 30.0
    }

    suspend operator fun invoke(
        currentLocation: Location,
        destinationLocation: Location,
        route: Route
    ): Result<Route> {
        val closestIndex = getClosestLocationIndex(currentLocation, route.polylines)
        val isOnRoute = route.polylines.map { LatLng(it.latitude, it.longitude) }.isLocationOnPath(
            LatLng(currentLocation.latitude, currentLocation.longitude),
            false,
            MAX_METERS
        )

        return if (isOnRoute) {
            val newPolylines = route.polylines.drop(closestIndex)
            Result.success(route.copy(polylines = newPolylines))
        } else {
            repository.getDirections(currentLocation, destinationLocation)
        }
    }

    private fun getClosestLocationIndex(
        currentLocation: Location,
        polylines: List<Location>
    ): Int {
        var minDistance = Float.MAX_VALUE
        var closestIndex = 0

        for (i in polylines.indices) {
            val distance = calculateDistanceBetweenLocations(currentLocation, polylines[i])
            if (distance < minDistance) {
                minDistance = distance
                closestIndex = i
            }
        }

        return closestIndex
    }
}

private fun calculateDistanceBetweenLocations(
    firstLocation: Location,
    secondLocation: Location
): Float {
    val result = FloatArray(1)
    android.location.Location.distanceBetween(
        firstLocation.latitude,
        firstLocation.longitude,
        secondLocation.latitude,
        secondLocation.longitude,
        result
    )
    return result.first()
}