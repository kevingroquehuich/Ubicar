package com.roque.ubicar.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Directions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.roque.ubicar.home.presentation.components.HomeButton
import com.roque.ubicar.home.presentation.components.HomeDirectionsInfo
import com.roque.ubicar.home.presentation.components.HomeMap
import com.roque.ubicar.home.presentation.components.HomePermissions

@Composable
fun HomeScreen(
    viewmodel: HomeViewmodel = hiltViewModel()
) {

    val state = viewmodel.state

    Scaffold(modifier = Modifier.fillMaxSize()) {

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
                    .padding(it)
            ) {
                HomeMap(
                    currentLocation = state.currentLocation,
                    carLocation = state.car?.location,
                    route = state.route,
                    modifier = Modifier.fillMaxSize()
                )

                when(state.carStatus) {
                    CarStatus.NOT_PARKED -> {
                        HomeButton(
                            onClick = { viewmodel.onEvent(HomeEvent.SaveCar) },
                            text = "Park Here",
                            imageVector = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 64.dp)
                                .padding(horizontal = 16.dp)
                        )
                    }
                    CarStatus.PARKED -> {
                        HomeButton(
                            onClick = { viewmodel.onEvent(HomeEvent.StartSearch) },
                            text = "Get directions",
                            imageVector = Icons.Outlined.Directions,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 64.dp)
                                .padding(horizontal = 16.dp)
                        )
                    }
                    CarStatus.SEARCHING -> {
                        HomeDirectionsInfo(
                            onClick = { viewmodel.onEvent(HomeEvent.StopSearch) },
                            distance = "1.5 km away",
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 64.dp)
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }

}