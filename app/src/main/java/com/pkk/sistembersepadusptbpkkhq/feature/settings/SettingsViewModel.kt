package com.pkk.sistembersepadusptbpkkhq.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pkk.sistembersepadusptbpkkhq.core.datastore.AppSettingsDataStore
import com.pkk.sistembersepadusptbpkkhq.core.datastore.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val appSettingsDataStore: AppSettingsDataStore,
) : ViewModel() {

    private val _biometricEnabled = MutableStateFlow(authDataStore.biometricEnabled)
    val biometricEnabled: StateFlow<Boolean> = _biometricEnabled.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(appSettingsDataStore.notificationsEnabled)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _themeMode = MutableStateFlow(appSettingsDataStore.darkMode)
    val themeMode: StateFlow<Boolean> = _themeMode.asStateFlow()

    val appVersion: String = "1.0.0"

    fun toggleBiometric() {
        val newValue = !_biometricEnabled.value
        _biometricEnabled.value = newValue
        authDataStore.biometricEnabled = newValue
    }

    fun toggleNotifications() {
        val newValue = !_notificationsEnabled.value
        _notificationsEnabled.value = newValue
        appSettingsDataStore.notificationsEnabled = newValue
    }

    fun toggleTheme() {
        val newValue = !_themeMode.value
        _themeMode.value = newValue
        appSettingsDataStore.darkMode = newValue
    }

    fun logout() {
        viewModelScope.launch {
            authDataStore.clear()
        }
    }
}
