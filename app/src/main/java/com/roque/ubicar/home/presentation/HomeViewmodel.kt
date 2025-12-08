package com.roque.ubicar.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roque.ubicar.home.domain.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val locationService: LocationService
): ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    init {
        viewModelScope.launch {
            locationService.getCurrentLocation()?.let {
                state = state.copy(currentLocation = it)
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.SaveCar -> {
                state = state.copy(carStatus = CarStatus.PARKED)
            }
            HomeEvent.StartSearch -> {
                state = state.copy(carStatus = CarStatus.SEARCHING)
            }
            HomeEvent.StopSearch -> {
                state = state.copy(carStatus = CarStatus.NOT_PARKED)
            }
        }
    }
}