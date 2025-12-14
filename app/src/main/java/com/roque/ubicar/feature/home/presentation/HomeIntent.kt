package com.roque.ubicar.feature.home.presentation

sealed class HomeIntent {
    data object SaveCar : HomeIntent()
    data object StartSearch : HomeIntent()
    data object StopSearch : HomeIntent()
    data class PermissionResult(val isPermissionsGranted: Boolean) : HomeIntent()
    data object ErrorSeen : HomeIntent()
}
