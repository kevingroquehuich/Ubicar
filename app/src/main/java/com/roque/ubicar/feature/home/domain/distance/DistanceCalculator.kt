package com.roque.ubicar.feature.home.domain.distance

import com.roque.ubicar.feature.home.domain.model.Location

interface DistanceCalculator {

    fun calculateDistanceBetweenLocations(
        startLocation: Location,
        endLocation: Location
    ): Float

    fun isLocationOnPath(
        path: List<Location>,
        location: Location,
        maxMeters: Double = 0.1
    ): Boolean
}