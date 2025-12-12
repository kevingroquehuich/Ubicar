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
import com.roque.ubicar.home.domain.usecase.GetPathToCarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val locationService: LocationService,
    private val repository: HomeRepository,
    private val getPathToCarUseCase: GetPathToCarUseCase
) : ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    private var locationJob: Job? = null

    init {
        getCurrentLocation()
        viewModelScope.launch {
            repository.getParkedCar()?.let {
                state = state.copy(car = it, carStatus = CarStatus.PARKED)
            }
        }
    }

    private fun getCurrentLocation() {
        viewModelScope.launch {
            locationService.getCurrentLocation()?.let {
                state = state.copy(currentLocation = it)
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SaveCar -> {

                viewModelScope.launch {
                    val currentLocation = locationService.getCurrentLocation()
                    state = state.copy(
                        currentLocation = currentLocation
                    )

                    currentLocation?.let {
                        val car = Car(location = Location(currentLocation.latitude, currentLocation.longitude))
                        repository.parkCar(car)
                        val parkedCar = repository.getParkedCar()
                        state = state.copy(
                            car = parkedCar,
                            carStatus = CarStatus.PARKED
                        )
                    }
                }
            }

            HomeEvent.StartSearch -> {

                state.car?.let { car ->
                    locationJob?.cancel()
                    locationJob = viewModelScope.launch {
                        val currentLocation = locationService.getCurrentLocation()
                        state = state.copy(
                            currentLocation = currentLocation
                        )

                        state.currentLocation?.let { currentLocation ->
                            repository.getDirections(
                                currentLocation = currentLocation,
                                destinationLocation = car.location
                            ).onSuccess { initialRoute ->
                                state = state.copy(route = initialRoute, carStatus = CarStatus.SEARCHING)

                                locationService.getLocationUpdates()
                                    .collectLatest { location ->
                                        state = state.copy(currentLocation = location)
                                        if (state.currentLocation != null && state.route != null) {
                                            getPathToCarUseCase(
                                                currentLocation = state.currentLocation!!,
                                                destinationLocation = car.location,
                                                route = state.route!!
                                            ).onSuccess { updatedRoute ->
                                                state = state.copy(route = updatedRoute)

                                                if (updatedRoute.distance < 5) {
                                                    viewModelScope.launch {
                                                        repository.deleteCar(car)
                                                    }
                                                    state = state.copy(
                                                        carStatus = CarStatus.NOT_PARKED,
                                                        car = null,
                                                        route = null,
                                                        errorMessage = "You've arrived at your vehicle!"
                                                    )
                                                    locationJob?.cancel()
                                                }
                                            }.onFailure { error ->
                                                state = state.copy(errorMessage = error.message ?: "Error calculating route")
                                            }
                                        }
                                    }
                            }.onFailure {
                                state = state.copy(errorMessage = it.message ?: "Error calculating route")
                            }
                        }
                    }
                }
            }

            HomeEvent.StopSearch -> {
                state.car?.let { car ->
                    viewModelScope.launch {
                        repository.deleteCar(car)
                    }
                    state = state.copy(carStatus = CarStatus.NOT_PARKED, car = null, route = null)
                    locationJob?.cancel()
                }
            }

            is HomeEvent.PermissionResult -> {
                state = state.copy(hasRequiredPermissions = event.isPermissionsGranted)
                if (event.isPermissionsGranted) getCurrentLocation()
            }

            HomeEvent.ErrorSeen -> {
                state = state.copy(errorMessage = null)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationJob?.cancel()
        locationService.stopLocationUpdates()
    }
}