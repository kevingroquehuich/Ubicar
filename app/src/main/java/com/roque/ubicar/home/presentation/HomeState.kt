package com.roque.ubicar.home.presentation

import com.roque.ubicar.home.domain.model.Car
import com.roque.ubicar.home.domain.model.Location

data class HomeState(
    val carStatus: CarStatus = CarStatus.NOT_PARKED,
    val currentLocation: Location? = null,
    val car: Car? = null
)

enum class CarStatus {
    NOT_PARKED,
    PARKED,
    SEARCHING,
}