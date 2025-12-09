package com.roque.ubicar.home.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CarEntity(
    @PrimaryKey
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
)