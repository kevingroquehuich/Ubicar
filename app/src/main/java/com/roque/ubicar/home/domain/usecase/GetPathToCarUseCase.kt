package com.roque.ubicar.home.domain.usecase

import com.roque.ubicar.home.domain.HomeRepository
import com.roque.ubicar.home.domain.distance.DistanceCalculator
import com.roque.ubicar.home.domain.model.Location
import com.roque.ubicar.home.domain.model.Route
import kotlin.math.roundToInt

class GetPathToCarUseCase(
    private val repository: HomeRepository,
    private val distanceCalculator: DistanceCalculator
) {

    companion object {
        const val BASE_THRESHOLD_METERS = 30.0
    }

    suspend operator fun invoke(
        currentLocation: Location,
        destinationLocation: Location,
        route: Route
    ): Result<Route> {
        val threshold = if (currentLocation.accuracy < 10f) {
            BASE_THRESHOLD_METERS
        } else {
            maxOf(BASE_THRESHOLD_METERS, currentLocation.accuracy * 2.0)
        }

        val closestIndex = getClosestLocationIndex(currentLocation, route.polylines)
        val isOnRoute = distanceCalculator.isLocationOnPath(route.polylines, currentLocation, threshold)

        return if (isOnRoute) {
            val newPolylines = route.polylines.drop(closestIndex)
            val distance = distanceCalculator.calculateDistanceBetweenLocations(currentLocation, newPolylines.last())
            Result.success(route.copy(
                polylines = newPolylines,
                distance = distance.roundToInt()
            ))
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
            val distance = distanceCalculator.calculateDistanceBetweenLocations(currentLocation, polylines[i])
            if (distance < minDistance) {
                minDistance = distance
                closestIndex = i
            }
        }

        return closestIndex
    }
}