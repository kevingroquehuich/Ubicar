package com.roque.ubicar.feature.authentication.presentation

sealed class LoginIntent {
    data object SignIn : LoginIntent()
}