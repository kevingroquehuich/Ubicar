package com.roque.ubicar

import androidx.lifecycle.ViewModel
import com.roque.ubicar.feature.authentication.domain.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
): ViewModel() {
    val isLoggedIn = authenticationRepository.isLoggedIn()
}