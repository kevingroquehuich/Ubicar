package com.roque.ubicar.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roque.ubicar.home.domain.HomeRepository
import com.roque.ubicar.home.domain.LocationService
import com.roque.ubicar.home.domain.model.Car
import com.roque.ubicar.home.domain.model.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val locationService: LocationService,
    private val repository: HomeRepository
): ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    init {
        viewModelScope.launch {
            locationService.getCurrentLocation()?.let {
                state = state.copy(currentLocation = it)
            }
        }

         viewModelScope.launch {
             repository.getParkedCar()?.let {
                 state = state.copy(car = it, carStatus = CarStatus.PARKED)
             }
         }
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.SaveCar -> {
                state.currentLocation?.let { currentLocation ->
                    viewModelScope.launch {
                        val car = Car(location = Location(currentLocation.latitude, currentLocation.longitude))
                        repository.parkCar(car)
                        val parkedCar = repository.getParkedCar()
                        state = state.copy(car = parkedCar)

                    }
                    state = state.copy(carStatus = CarStatus.PARKED)
                }
            }

            HomeEvent.StartSearch -> {
                state = state.copy(carStatus = CarStatus.SEARCHING)
            }

            HomeEvent.StopSearch -> {
                state.car?.let { car ->
                    viewModelScope.launch {
                        repository.deleteCar(car)
                    }
                    state = state.copy(carStatus = CarStatus.NOT_PARKED)
                }
            }
        }
    }
}