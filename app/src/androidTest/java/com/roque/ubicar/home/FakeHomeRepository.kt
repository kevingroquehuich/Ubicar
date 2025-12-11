package com.roque.ubicar.home

import com.roque.ubicar.home.domain.HomeRepository
import com.roque.ubicar.home.domain.model.Car
import com.roque.ubicar.home.domain.model.Location
import com.roque.ubicar.home.domain.model.Route

class FakeHomeRepository : HomeRepository {

    private var parkedCar: Car? = null

    override suspend fun parkCar(car: Car) {
        parkedCar = car
    }

    override suspend fun getParkedCar(): Car? = parkedCar

    override suspend fun deleteCar(car: Car) {
        parkedCar = null
    }

    override suspend fun getDirections(
        currentLocation: Location,
        destinationLocation: Location
    ): Result<Route>  = Result.success(Route(3000, emptyList()))
}