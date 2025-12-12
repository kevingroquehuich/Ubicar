package com.roque.ubicar.home.data.repository

import com.roque.ubicar.home.data.extension.resultOf
import com.roque.ubicar.home.data.local.dao.HomeDao
import com.roque.ubicar.home.data.mapper.toDomain
import com.roque.ubicar.home.data.mapper.toEntity
import com.roque.ubicar.home.data.mapper.toRoute
import com.roque.ubicar.home.data.remote.GoogleDirectionsApi
import com.roque.ubicar.home.domain.HomeRepository
import com.roque.ubicar.home.domain.model.Car
import com.roque.ubicar.home.domain.model.Location
import com.roque.ubicar.home.domain.model.Route

class HomeRepositoryImpl(
    private val dao: HomeDao,
    private val api: GoogleDirectionsApi
): HomeRepository {

    override suspend fun parkCar(car: Car) {
        dao.insertCar(car.toEntity())
    }

    override suspend fun getParkedCar(): Car? = dao.getParkedCar()?.toDomain()

    override suspend fun deleteCar(car: Car) {
        dao.deleteCar(car.toEntity())
    }

    override suspend fun getDirections(
        currentLocation: Location,
        destinationLocation: Location
    ): Result<Route> {
        val origin = "${currentLocation.latitude},${currentLocation.longitude}"
        val destination = "${destinationLocation.latitude},${destinationLocation.longitude}"

        return resultOf {
            api.getDirections(origin, destination, "walking").toRoute()
        }
    }
}