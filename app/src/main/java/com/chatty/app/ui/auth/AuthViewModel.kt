package com.chatty.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatty.app.core.di.AppContainer
import com.chatty.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(
    private val repository: AuthRepository = AppContainer.authRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Email and password required")
            return
        }
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            val result = repository.login(email.trim(), password)
            _uiState.value = result.fold(
                onSuccess = { AuthUiState.Success },
                onFailure = { AuthUiState.Error(it.message ?: "Login failed") }
            )
        }
    }

    fun register(email: String, password: String) {
        if (email.isBlank() || password.length < 6) {
            _uiState.value = AuthUiState.Error("Password must be 6+ characters")
            return
        }
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            val result = repository.register(email.trim(), password)
            _uiState.value = result.fold(
                onSuccess = { AuthUiState.Success },
                onFailure = { AuthUiState.Error(it.message ?: "Registration failed") }
            )
        }
    }

    fun resetState() { _uiState.value = AuthUiState.Idle }
}
