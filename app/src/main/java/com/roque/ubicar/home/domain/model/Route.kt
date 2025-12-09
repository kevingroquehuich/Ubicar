package com.roque.ubicar.home.domain.model

data class Route(
    val distance: String,
    val duration: String,
    val paths: List<Path>,
    val polylines: List<Location>,
)
