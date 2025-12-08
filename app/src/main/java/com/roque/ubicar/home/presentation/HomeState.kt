package com.roque.ubicar.home.presentation

import android.location.Location

data class HomeState(
    val carStatus: CarStatus = CarStatus.NOT_PARKED,
    val currentLocation: Location? = null
)

enum class CarStatus {
    NOT_PARKED,
    PARKED,
    SEARCHING,
}