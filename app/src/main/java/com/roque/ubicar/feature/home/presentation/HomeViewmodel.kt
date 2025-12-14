package com.roque.ubicar.feature.home.presentation

import androidx.lifecycle.viewModelScope
import com.roque.ubicar.core.presentation.BaseViewModel
import com.roque.ubicar.feature.home.domain.repository.HomeRepository
import com.roque.ubicar.feature.home.domain.repository.LocationService
import com.roque.ubicar.feature.home.domain.model.Car
import com.roque.ubicar.feature.home.domain.model.Location
import com.roque.ubicar.feature.home.domain.usecase.GetPathToCarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationService: LocationService,
    private val repository: HomeRepository,
    private val getPathToCarUseCase: GetPathToCarUseCase
) : BaseViewModel<HomeUiState, HomeIntent, HomeSideEffect>() {

    private var locationJob: Job? = null

    override fun createInitialState(): HomeUiState = HomeUiState()

    init {
        startLocationUpdates()
        viewModelScope.launch {
            repository.getParkedCar()?.let { car ->
                setState { 
                    copy(
                        car = car,
                        carStatus = if (car.isSearching) CarStatus.SEARCHING else CarStatus.PARKED
                    ) 
                }
                
                // Restaurar búsqueda activa si estaba buscando
                if (car.isSearching) {
                    setIntent(HomeIntent.StartSearch)
                }
            }
        }
    }

    override fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.SaveCar -> handleSaveCar()
            is HomeIntent.StartSearch -> handleStartSearch()
            is HomeIntent.StopSearch -> handleStopSearch()
            is HomeIntent.PermissionResult -> handlePermissionResult(intent.isPermissionsGranted)
            is HomeIntent.ErrorSeen -> setState { copy(errorMessage = null) }
        }
    }

    private fun startLocationUpdates() {
        viewModelScope.launch {
            locationService.getLocationUpdates().collectLatest { location ->
                location?.let {
                    setState { copy(currentLocation = it) }
                }
            }
        }
    }

    private fun handleSaveCar() {
        viewModelScope.launch {
            val currentLocation = locationService.getCurrentLocation()
            setState { copy(currentLocation = currentLocation) }

            currentLocation?.let {
                val car = Car(
                    location = Location(
                        currentLocation.latitude,
                        currentLocation.longitude
                    )
                )
                repository.parkCar(car)
                val parkedCar = repository.getParkedCar()
                setState {
                    copy(
                        car = parkedCar,
                        carStatus = CarStatus.PARKED
                    )
                }
            }
        }
    }

    private fun handleStartSearch() {
        uiState.value.car?.let { car ->
            // Guardar estado de búsqueda activa
            viewModelScope.launch {
                repository.parkCar(car.copy(isSearching = true))
            }
            
            locationJob?.cancel()
            locationJob = viewModelScope.launch {
                val currentLocation = locationService.getCurrentLocation()
                setState { copy(currentLocation = currentLocation) }

                uiState.value.currentLocation?.let { currentLocation ->
                    repository.getDirections(
                        currentLocation = currentLocation,
                        destinationLocation = car.location
                    ).onSuccess { initialRoute ->
                        setState { copy(route = initialRoute, carStatus = CarStatus.SEARCHING) }

                        locationService.getLocationUpdates()
                            .collectLatest { location ->
                                setState { copy(currentLocation = location) }
                                if (uiState.value.currentLocation != null && uiState.value.route != null) {
                                    getPathToCarUseCase(
                                        currentLocation = uiState.value.currentLocation!!,
                                        destinationLocation = car.location,
                                        route = uiState.value.route!!
                                    ).onSuccess { updatedRoute ->
                                        setState { copy(route = updatedRoute) }

                                        if (updatedRoute.distance < 5) {
                                            viewModelScope.launch {
                                                repository.deleteCar(car)
                                            }
                                            setState {
                                                copy(
                                                    carStatus = CarStatus.NOT_PARKED,
                                                    car = null,
                                                    route = null
                                                )
                                            }
                                            setEvent { HomeSideEffect.VehicleArrived }
                                            locationJob?.cancel()
                                        }
                                    }.onFailure { error ->
                                        setEvent {
                                            HomeSideEffect.ShowMessage(
                                                error.message ?: "Error calculating route"
                                            )
                                        }
                                    }
                                }
                            }
                    }.onFailure {
                        setEvent {
                            HomeSideEffect.ShowMessage(
                                it.message ?: "Error calculating route"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun handleStopSearch() {
        uiState.value.car?.let { car ->
            viewModelScope.launch {
                // Guardar que ya no está buscando antes de eliminar
                repository.parkCar(car.copy(isSearching = false))
                repository.deleteCar(car)
            }
            setState { copy(carStatus = CarStatus.NOT_PARKED, car = null, route = null) }
            locationJob?.cancel()
        }
    }

    private fun handlePermissionResult(isGranted: Boolean) {
        setState { copy(hasRequiredPermissions = isGranted) }
        // startLocationUpdates() already running, will update automatically
    }

    override fun onCleared() {
        super.onCleared()
        locationJob?.cancel()
        locationService.stopLocationUpdates()
    }
}
