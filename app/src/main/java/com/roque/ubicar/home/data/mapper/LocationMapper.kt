package com.roque.ubicar.home.data.mapper

import android.location.Location

fun Location.toDomain(): com.roque.ubicar.home.domain.model.Location{
    return com.roque.ubicar.home.domain.model.Location(
        latitude = this.latitude,
        longitude = this.longitude

    )
}