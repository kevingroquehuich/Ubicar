package com.roque.ubicar.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Directions
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.roque.ubicar.R
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.roque.ubicar.home.presentation.components.HomeButton
import com.roque.ubicar.home.presentation.components.HomeDirectionsInfo
import com.roque.ubicar.home.presentation.components.HomeMap
import com.roque.ubicar.home.presentation.components.HomePermissions

@Composable
fun HomeScreen(
    viewmodel: HomeViewmodel = hiltViewModel()
) {

    val state = viewmodel.state
    val snackBarHostState = remember { SnackbarHostState() }
    val cameraPositionState = rememberCameraPositionState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { error ->
            snackBarHostState.showSnackbar(error)
            viewmodel.onEvent(HomeEvent.ErrorSeen)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->

        if (!state.hasRequiredPermissions) {
            HomePermissions(
                permissions = listOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                onPermissionResult = { isGranted ->
                    viewmodel.onEvent(HomeEvent.PermissionResult(isGranted))
                }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                HomeMap(
                    currentLocation = state.currentLocation,
                    carLocation = state.car?.location,
                    route = state.route,
                    cameraPositionState = cameraPositionState,
                    modifier = Modifier.fillMaxSize()
                )

                MapControls(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 16.dp, end = 16.dp),
                    myLocationOnClick = {
                        state.currentLocation?.let {
                            scope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(it.latitude, it.longitude),
                                        18f
                                    )
                                )
                            }
                        }
                    }
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
                ) {

                    Column {
                        MapZoomControls(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(bottom = 16.dp),
                            zoomInOnClick = {
                                scope.launch {
                                    cameraPositionState.animate(CameraUpdateFactory.zoomIn())
                                }
                            },
                            zoomOutOnClick = {
                                scope.launch {
                                    cameraPositionState.animate(CameraUpdateFactory.zoomOut())
                                }
                            }
                        )

                        when(state.carStatus) {
                            CarStatus.NOT_PARKED -> {
                                HomeButton(
                                    onClick = { viewmodel.onEvent(HomeEvent.SaveCar) },
                                    text = stringResource(R.string.park_here),
                                    imageVector = null,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            CarStatus.PARKED -> {
                                HomeButton(
                                    onClick = { viewmodel.onEvent(HomeEvent.StartSearch) },
                                    text = stringResource(R.string.get_directions),
                                    imageVector = Icons.Outlined.Directions,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            CarStatus.SEARCHING -> {
                                HomeDirectionsInfo(
                                    onClick = { viewmodel.onEvent(HomeEvent.StopSearch) },
                                    distance = meterToText(state.route?.distance),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MapControls(
    modifier: Modifier,
    myLocationOnClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF2B2B2B))
    ) {
        IconButton(
            onClick = { },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Map,
                contentDescription = "Map Type",
            )
        }
        IconButton(
            onClick = { myLocationOnClick() },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.NearMe,
                contentDescription = "My location",
            )
        }
    }
}

@Composable
private fun MapZoomControls(
    modifier: Modifier,
    zoomInOnClick: () -> Unit = {},
    zoomOutOnClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF2B2B2B)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { zoomInOnClick() },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Zoom In"
            )
        }
        IconButton(
            onClick = { zoomOutOnClick() },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Remove,
                contentDescription = "Zoom Out",
            )
        }
    }
}