package com.roque.ubicar.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(): ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.SaveCar -> {
                state = state.copy(carStatus = CarStatus.PARKED)
            }

            HomeEvent.StartSearch -> TODO()
            HomeEvent.StopSearch -> TODO()
        }
    }
}