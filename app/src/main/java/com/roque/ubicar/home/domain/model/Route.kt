package com.roque.ubicar.home.domain.model

data class Route(
    val distance: Int,
    val polylines: List<Location>,
)
