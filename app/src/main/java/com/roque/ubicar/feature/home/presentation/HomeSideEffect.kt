package com.roque.ubicar.feature.home.presentation

sealed class HomeSideEffect {
    data class ShowMessage(val message: String) : HomeSideEffect()
    data object VehicleArrived : HomeSideEffect()
}
