package com.pkk.sistembersepadusptbpkkhq.feature.approver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pkk.sistembersepadusptbpkkhq.core.datastore.AuthDataStore
import com.pkk.sistembersepadusptbpkkhq.core.domain.Application
import com.pkk.sistembersepadusptbpkkhq.core.domain.repository.SptbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ApproverUiState {
    data object Loading : ApproverUiState()
    data object Empty : ApproverUiState()
    data class Content(val applications: List<Application>) : ApproverUiState()
}

@HiltViewModel
class ApproverViewModel @Inject constructor(
    private val repository: SptbRepository,
    private val authDataStore: AuthDataStore,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ApproverUiState>(ApproverUiState.Loading)
    val uiState: StateFlow<ApproverUiState> = _uiState.asStateFlow()

    private val _selectedApplication = MutableStateFlow<Application?>(null)
    val selectedApplication: StateFlow<Application?> = _selectedApplication.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    init {
        loadPendingApplications()
    }

    fun loadPendingApplications() {
        viewModelScope.launch {
            _uiState.value = ApproverUiState.Loading
            try {
                val role = authDataStore.userRole ?: "PELULUS"
                val name = authDataStore.userName ?: ""
                val allApps = repository.getApplications(role, name)
                val pending = allApps.filter {
                    it.kelulusan.isNullOrEmpty() && it.syorStatus.equals("SOKONG", ignoreCase = true)
                }
                _uiState.value = if (pending.isEmpty()) ApproverUiState.Empty
                else ApproverUiState.Content(pending)
            } catch (_: Exception) {
                _uiState.value = ApproverUiState.Empty
            }
        }
    }

    fun selectApplication(app: Application) {
        _selectedApplication.value = app
    }

    fun clearSelection() {
        _selectedApplication.value = null
    }

    fun approve(reason: String) {
        val app = _selectedApplication.value ?: return
        viewModelScope.launch {
            _isProcessing.value = true
            try {
                repository.approveApplication(
                    mapOf(
                        "id" to app.id.toString(),
                        "kelulusan" to "LULUS",
                        "alasan" to reason,
                        "pelulus" to (authDataStore.userName ?: ""),
                        "role" to (authDataStore.userRole ?: "PELULUS"),
                    )
                )
                _selectedApplication.value = null
                loadPendingApplications()
            } catch (_: Exception) {
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun reject(reason: String) {
        val app = _selectedApplication.value ?: return
        viewModelScope.launch {
            _isProcessing.value = true
            try {
                repository.approveApplication(
                    mapOf(
                        "id" to app.id.toString(),
                        "kelulusan" to "TOLAK",
                        "alasan" to reason,
                        "pelulus" to (authDataStore.userName ?: ""),
                        "role" to (authDataStore.userRole ?: "PELULUS"),
                    )
                )
                _selectedApplication.value = null
                loadPendingApplications()
            } catch (_: Exception) {
            } finally {
                _isProcessing.value = false
            }
        }
    }
}
