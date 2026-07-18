package com.kuskop.sptb.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuskop.sptb.core.datastore.AuthDataStore
import com.kuskop.sptb.core.domain.User
import com.kuskop.sptb.core.domain.repository.SptbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    data object Loading : AuthState()
    data object NeedsGoogleSignIn : AuthState()
    data object NeedsBiometric : AuthState()
    data class Authenticated(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val repository: SptbRepository,
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _isLoggingIn = MutableStateFlow(false)
    val isLoggingIn: StateFlow<Boolean> = _isLoggingIn.asStateFlow()

    init {
        checkExistingSession()
    }

    private fun checkExistingSession() {
        if (authDataStore.isLoggedIn && authDataStore.biometricEnabled) {
            _authState.value = AuthState.NeedsBiometric
        } else if (authDataStore.isLoggedIn) {
            authenticateWithServer(authDataStore.userEmail ?: return)
        } else {
            _authState.value = AuthState.NeedsGoogleSignIn
        }
    }

    fun onBiometricSuccess() {
        authDataStore.biometricFailCount = 0
        val email = authDataStore.userEmail ?: run {
            _authState.value = AuthState.NeedsGoogleSignIn
            return
        }
        authenticateWithServer(email)
    }

    fun onBiometricFailed() {
        val count = authDataStore.biometricFailCount + 1
        authDataStore.biometricFailCount = count
        if (count >= 3) {
            authDataStore.clear()
            _authState.value = AuthState.NeedsGoogleSignIn
        }
    }

    fun onBiometricFallback() {
        _authState.value = AuthState.NeedsGoogleSignIn
    }

    fun onGoogleSignInSuccess(email: String, name: String, idToken: String) {
        viewModelScope.launch {
            _isLoggingIn.value = true
            try {
                authDataStore.authToken = idToken
                authDataStore.userEmail = email
                authDataStore.userName = name
                authDataStore.biometricEnabled = true
                authDataStore.biometricFailCount = 0
                authenticateWithServer(email)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Ralat log masuk")
            } finally {
                _isLoggingIn.value = false
            }
        }
    }

    private fun authenticateWithServer(email: String) {
        viewModelScope.launch {
            try {
                val user = repository.checkAuth(email)
                if (user != null) {
                    authDataStore.userEmail = user.email
                    authDataStore.userName = user.name
                    authDataStore.userRole = user.role
                    _authState.value = AuthState.Authenticated(user)
                } else {
                    authDataStore.clear()
                    _authState.value = AuthState.NeedsGoogleSignIn
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Ralat sambungan")
            }
        }
    }

    fun logout() {
        authDataStore.clear()
        _authState.value = AuthState.NeedsGoogleSignIn
    }

    fun resetError() {
        if (_authState.value is AuthState.Error) {
            checkExistingSession()
        }
    }
}
