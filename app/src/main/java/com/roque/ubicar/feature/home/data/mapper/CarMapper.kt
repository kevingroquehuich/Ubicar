package com.roque.ubicar.feature.home.data.mapper

import com.roque.ubicar.feature.home.data.local.entity.CarEntity
import com.roque.ubicar.feature.home.domain.model.Car
import com.roque.ubicar.feature.home.domain.model.Location

fun CarEntity.toDomain(): Car = Car(
    id = this.id,
    location = Location(
        latitude = this.latitude,
        longitude = this.longitude
    )
)

fun Car.toEntity(): CarEntity = CarEntity(
    id = this.id ?: 0,
    latitude = this.location.latitude,
    longitude = this.location.longitude
)

