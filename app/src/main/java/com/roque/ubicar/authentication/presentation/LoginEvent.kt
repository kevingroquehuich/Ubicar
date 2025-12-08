package com.roque.ubicar.authentication.presentation

import android.content.Context

sealed interface LoginEvent {
    data class SignIn(val context: Context): LoginEvent
}