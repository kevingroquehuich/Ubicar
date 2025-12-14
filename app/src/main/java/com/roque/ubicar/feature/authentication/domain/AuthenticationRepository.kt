package com.roque.ubicar.feature.authentication.domain

interface AuthenticationRepository {
    suspend fun oneTapLogin(): Result<Unit>
    fun isLoggedIn(): Boolean
}