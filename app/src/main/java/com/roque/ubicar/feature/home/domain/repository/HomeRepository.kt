package com.roque.ubicar.feature.home.domain.repository

import com.roque.ubicar.feature.home.domain.model.Car
import com.roque.ubicar.feature.home.domain.model.Location
import com.roque.ubicar.feature.home.domain.model.Route

interface HomeRepository {

    suspend fun parkCar(car: Car)
    suspend fun getParkedCar(): Car?
    suspend fun deleteCar(car: Car)
    suspend fun getDirections(currentLocation: Location, destinationLocation: Location): Result<Route>
}