package com.roque.ubicar.home.data.mapper

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun Location.toDomain(): com.roque.ubicar.home.domain.model.Location {
    return com.roque.ubicar.home.domain.model.Location(
        latitude = this.latitude,
        longitude = this.longitude,
        accuracy = if (this.hasAccuracy()) this.accuracy else 10f
    )
}

fun LatLng.toLocation(): com.roque.ubicar.home.domain.model.Location{
    return com.roque.ubicar.home.domain.model.Location(
        latitude = this.latitude,
        longitude = this.longitude

    )
}

