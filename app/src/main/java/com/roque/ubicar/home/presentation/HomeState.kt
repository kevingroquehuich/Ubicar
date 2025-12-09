package com.roque.ubicar.home.presentation

import com.roque.ubicar.home.domain.model.Car
import com.roque.ubicar.home.domain.model.Location
import com.roque.ubicar.home.domain.model.Route

data class HomeState(
    val carStatus: CarStatus = CarStatus.NOT_PARKED,
    val currentLocation: Location? = null,
    val car: Car? = null,
    val route: Route? = null
)

enum class CarStatus {
    NOT_PARKED,
    PARKED,
    SEARCHING,
}