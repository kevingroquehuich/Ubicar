package com.roque.ubicar.feature.home.presentation

fun meterToText(meters: Int?): String? {
    if (meters == null) return null

    if (meters >= 1000)
        return "${meters / 100} km"

    return "${meters} mts"
}