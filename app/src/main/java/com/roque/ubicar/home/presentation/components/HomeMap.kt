package com.roque.ubicar.home.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.roque.ubicar.home.domain.model.Location


@Composable
fun HomeMap(
    currentLocation: Location?,
    carLocation: Location?,
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
    ) {  }
}

@Preview
@Composable
fun HomeMapPreview() {
    HomeMap(null, null)
}