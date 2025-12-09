package com.roque.ubicar.home.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Bounds(
    @SerialName("northeast")
    val northeast: Northeast,
    @SerialName("southwest")
    val southwest: Southwest
)