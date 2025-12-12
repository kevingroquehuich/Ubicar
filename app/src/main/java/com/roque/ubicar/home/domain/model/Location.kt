package com.roque.ubicar.home.domain.model

data class Location(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float = 10f,
    val bearing: Float = 0f // Direction in degrees (0-360)
)
