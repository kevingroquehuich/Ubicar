package com.roque.ubicar.feature.home.presentation

import android.Manifest
import android.content.Context
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.roque.ubicar.R
import com.roque.ubicar.core.presentation.extension.collectWithLifeCycle
import com.roque.ubicar.feature.home.presentation.components.HomeButton
import com.roque.ubicar.feature.home.presentation.components.HomeDirectionsInfo
import com.roque.ubicar.feature.home.presentation.components.HomeMap
import com.roque.ubicar.feature.home.presentation.components.HomePermissions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val cameraPositionState = rememberCameraPositionState()
    val scope = rememberCoroutineScope()
    val context: Context = LocalContext.current

    HandleSideEffects(
        sideEffect = viewModel.effect,
        snackBarHostState = snackBarHostState,
        context = context
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->

        if (!uiState.hasRequiredPermissions) {
            HomePermissions(
                permissions = listOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                onPermissionResult = { isGranted ->
                    viewModel.setIntent(HomeIntent.PermissionResult(isGranted))
                }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                HomeMap(
                    currentLocation = uiState.currentLocation,
                    carLocation = uiState.car?.location,
                    route = uiState.route,
                    cameraPositionState = cameraPositionState,
                    modifier = Modifier.fillMaxSize()
                )

                MapControls(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 16.dp, end = 16.dp),
                    myLocationOnClick = {
                        uiState.currentLocation?.let {
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

                        when(uiState.carStatus) {
                            CarStatus.NOT_PARKED -> {
                                HomeButton(
                                    onClick = { viewModel.setIntent(HomeIntent.SaveCar) },
                                    text = stringResource(R.string.park_here),
                                    imageVector = null,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            CarStatus.PARKED -> {
                                HomeButton(
                                    onClick = { viewModel.setIntent(HomeIntent.StartSearch) },
                                    text = stringResource(R.string.get_directions),
                                    imageVector = Icons.Outlined.Directions,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            CarStatus.SEARCHING -> {
                                HomeDirectionsInfo(
                                    onClick = { viewModel.setIntent(HomeIntent.StopSearch) },
                                    distance = meterToText(uiState.route?.distance),
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
fun HandleSideEffects(
    sideEffect: Flow<HomeSideEffect>,
    snackBarHostState: SnackbarHostState,
    context: Context
) {
    sideEffect.collectWithLifeCycle { effect ->
        when(effect) {
            is HomeSideEffect.ShowMessage -> {
                snackBarHostState.showSnackbar(effect.message)
            }
            HomeSideEffect.VehicleArrived -> {
                snackBarHostState.showSnackbar(context.getString(R.string.you_ve_arrived_at_your_vehicle))
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
            .background(MaterialTheme.colorScheme.surface)
    ) {
        IconButton(
            onClick = { },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surface
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
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surface
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
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { zoomInOnClick() },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surface
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
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Remove,
                contentDescription = "Zoom Out",
            )
        }
    }
}