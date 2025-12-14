package com.roque.ubicar.feature.home.presentation

import com.roque.ubicar.feature.home.domain.model.Car
import com.roque.ubicar.feature.home.domain.model.Location
import com.roque.ubicar.feature.home.domain.model.Route

data class HomeUiState(
    val carStatus: CarStatus = CarStatus.NOT_PARKED,
    val currentLocation: Location? = null,
    val car: Car? = null,
    val route: Route? = null,
    val hasRequiredPermissions: Boolean = false,
    val errorMessage: String? = null
)

enum class CarStatus {
    NOT_PARKED,
    PARKED,
    SEARCHING,
}
