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
): ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    private lateinit var locationJob: Job

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
                    locationJob = viewModelScope.launch {
                        val currentLocation = locationService.getCurrentLocation()
                        state = state.copy(
                            currentLocation = currentLocation
                        )

                        state.currentLocation?.let { currentLocation ->
                            repository.getDirections(
                                currentLocation = currentLocation,
                                destinationLocation = car.location
                            ).onSuccess {
                                state = state.copy(route = it, carStatus = CarStatus.SEARCHING)

                                locationService.getLocationUpdates().collectLatest {
                                    state = state.copy(currentLocation = it)
                                    if (state.currentLocation != null && state.route != null) {
                                        getPathToCarUseCase(
                                            currentLocation = state.currentLocation!!,
                                            destinationLocation = car.location,
                                            route = state.route!!
                                        ).onSuccess {
                                            state = state.copy(route = it)
                                        }.onFailure {

                                        }
                                    }
                                }
                            }.onFailure {
                                //TODO: Handle error
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
                    locationJob.cancel()
                }
            }

            is HomeEvent.PermissionResult -> {
                state = state.copy(hasRequiredPermissions = event.isPermissionsGranted)
            }
        }
    }
}