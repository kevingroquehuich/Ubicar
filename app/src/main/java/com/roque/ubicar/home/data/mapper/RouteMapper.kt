package com.roque.ubicar.home.data.mapper

import com.google.maps.android.ktx.utils.toLatLngList
import com.roque.ubicar.home.data.remote.dto.DirectionsResponse
import com.roque.ubicar.home.domain.model.Location
import com.roque.ubicar.home.domain.model.Path
import com.roque.ubicar.home.domain.model.Route

fun DirectionsResponse.toRoute(): Route {
    val firstRoute = this.routes.first()
    val firstLeg = firstRoute.legs.first()
    val paths = mutableListOf<Path>()

    val routePoints = firstLeg.steps.flatMap {
        val polylines = it.polyline.points.toLatLngList()
        val path = Path(
            location = Location(latitude = it.startLocation.lat, longitude = it.startLocation.lng),
            distance = it.distance.value,
            street = it.htmlInstructions,
            duration = it.duration.value,
            steps = polylines.size
        )
        paths.add(path)
        polylines
    }

    return Route(
        distance = firstLeg.distance.value,
        duration = firstLeg.duration.text,
        paths = paths,
        polylines = routePoints.map { it.toLocation() }
    )
}