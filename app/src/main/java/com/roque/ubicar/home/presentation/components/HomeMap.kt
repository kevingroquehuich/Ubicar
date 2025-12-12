package com.roque.ubicar.home.presentation.components

import android.content.Context
import android.graphics.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.roque.ubicar.R
import com.roque.ubicar.home.domain.model.Location
import com.roque.ubicar.home.domain.model.Route


@Composable
fun HomeMap(
    currentLocation: Location?,
    carLocation: Location?,
    route: Route?,
    cameraPositionState: CameraPositionState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val mapStyle = remember {
        MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
    }

    LaunchedEffect(key1 = currentLocation) {
        currentLocation?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLng(
                    LatLng(it.latitude, it.longitude)
                )
            )
        }
    }

    GoogleMap(
        modifier = modifier,
        properties = MapProperties(
            isMyLocationEnabled = true,
            maxZoomPreference = 20f,
            minZoomPreference = 15f,
            mapStyleOptions = mapStyle
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false
        ),
        cameraPositionState = cameraPositionState
    ) {
        carLocation?.let {
            CarMarker(position = it)
        }

        route?.let { route ->
            Polyline(
                points = route.polylines.map { LatLng(it.latitude, it.longitude) },
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun CarMarker(position: Location) {
    val context = LocalContext.current

    val state = remember {
        MarkerState(LatLng(position.latitude, position.longitude))
    }

    Marker(
        state = state,
        icon = bitmapDescriptorFromVector(context, R.drawable.ic_marker)
    )
}

private fun bitmapDescriptorFromVector(context: Context, imageId: Int): BitmapDescriptor? {
    val vectorDrawable = ContextCompat.getDrawable(context, imageId) ?: return null
    vectorDrawable.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
    val bitmap = createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
