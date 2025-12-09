package com.roque.ubicar.home.domain

import com.roque.ubicar.home.domain.model.Car

interface HomeRepository {

    suspend fun parkCar(car: Car)

    suspend fun getParkedCar(): Car?

    suspend fun deleteCar(car: Car)
}