package com.roque.ubicar.home.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.roque.ubicar.home.domain.model.Location
import com.roque.ubicar.home.domain.model.Route


@Composable
fun HomeMap(
    currentLocation: Location?,
    carLocation: Location?,
    route: Route?,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(key1 = currentLocation) {
        currentLocation?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(it.latitude, it.longitude),
                    17f
                )
            )
        }
    }

    GoogleMap(
        modifier = modifier,
        properties = MapProperties(
            isMyLocationEnabled = true,
            maxZoomPreference = 20f,
            minZoomPreference = 15f
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false
        ),
        cameraPositionState = cameraPositionState
    ) {
        carLocation?.let {
            CarMarker(position = it)
        }

        route?.let { route ->
            Polyline(points = route.polylines.map { LatLng(it.latitude, it.longitude) })
        }
    }
}

@Composable
private fun CarMarker(position: Location) {
    val state = remember {
        MarkerState(LatLng(position.latitude, position.longitude))
    }

    Marker(state = state)
}