package com.roque.ubicar.home.data.repository

import com.roque.ubicar.home.data.local.dao.HomeDao
import com.roque.ubicar.home.data.mapper.toDomain
import com.roque.ubicar.home.data.mapper.toEntity
import com.roque.ubicar.home.domain.HomeRepository
import com.roque.ubicar.home.domain.model.Car

class HomeRepositoryImpl(
    private val dao: HomeDao
): HomeRepository {

    override suspend fun parkCar(car: Car) {
        dao.insertCar(car.toEntity())
    }

    override suspend fun getParkedCar(): Car? = dao.getParkedCar()?.toDomain()

    override suspend fun deleteCar(car: Car) {
        dao.deleteCar(car.toEntity())
    }
}