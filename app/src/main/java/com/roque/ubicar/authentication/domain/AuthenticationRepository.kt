package com.roque.ubicar.authentication.domain

interface AuthenticationRepository {
    suspend fun oneTapLogin(): Result<Unit>
}