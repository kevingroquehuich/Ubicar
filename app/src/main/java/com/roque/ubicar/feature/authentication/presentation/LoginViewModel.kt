package com.roque.ubicar.feature.authentication.presentation

import androidx.lifecycle.viewModelScope
import com.roque.ubicar.core.presentation.BaseViewModel
import com.roque.ubicar.feature.authentication.domain.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthenticationRepository
) : BaseViewModel<LoginUiState, LoginIntent, LoginSideEffect>() {

    override fun createInitialState(): LoginUiState = LoginUiState()

    override fun handleIntent(intent: LoginIntent) {
        when(intent) {
            is LoginIntent.SignIn -> {
                viewModelScope.launch {
                    setState { copy(isLoading = true, error = null) }
                    repository.oneTapLogin()
                        .onSuccess {
                            setState { copy(isLoading = false) }
                            setEvent { LoginSideEffect.NavigateToHome }
                        }
                        .onFailure { exception ->
                            setState { copy(isLoading = false, error = exception.message) }
                            setEvent {
                                LoginSideEffect.ShowMessage(exception.message ?: "Something went wrong")
                            }
                        }
                }
            }
        }
    }
}