package com.roque.ubicar.feature.authentication.presentation

sealed class LoginSideEffect {
    data object NavigateToHome : LoginSideEffect()
    data class ShowMessage(val message: String) : LoginSideEffect()
}