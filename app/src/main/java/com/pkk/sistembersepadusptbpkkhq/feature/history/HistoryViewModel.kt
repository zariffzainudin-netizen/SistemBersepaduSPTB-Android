package com.pkk.sistembersepadusptbpkkhq.feature.history

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

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: SptbRepository,
    private val authDataStore: AuthDataStore,
) : ViewModel() {

    private val _historyItems = MutableStateFlow<List<Application>>(emptyList())
    val historyItems: StateFlow<List<Application>> = _historyItems.asStateFlow()

    private val _filteredItems = MutableStateFlow<List<Application>>(emptyList())
    val filteredItems: StateFlow<List<Application>> = _filteredItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _statusFilter = MutableStateFlow("Semua")
    val statusFilter: StateFlow<String> = _statusFilter.asStateFlow()

    private val _jenisFilter = MutableStateFlow("Semua")
    val jenisFilter: StateFlow<String> = _jenisFilter.asStateFlow()

    val statusOptions = listOf("Semua", "Lulus", "Tolak", "Proses")
    val jenisOptions = listOf("Semua", "Baru", "Pembaharuan", "Ubah Maklumat", "Ubah Gred")

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val role = authDataStore.userRole ?: "PENGESYOR"
                val name = authDataStore.userName ?: ""
                val apps = repository.getApplications(role, name)
                _historyItems.value = apps
                applyFilters()
            } catch (_: Exception) {
                _historyItems.value = emptyList()
                _filteredItems.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setStatusFilter(status: String) {
        _statusFilter.value = status
        applyFilters()
    }

    fun setJenisFilter(jenis: String) {
        _jenisFilter.value = jenis
        applyFilters()
    }

    private fun applyFilters() {
        val status = _statusFilter.value
        val jenis = _jenisFilter.value
        _filteredItems.value = _historyItems.value.filter { app ->
            val matchesStatus = status == "Semua" || app.kelulusan.equals(status, ignoreCase = true)
            val matchesJenis = jenis == "Semua" || app.jenis.equals(jenis, ignoreCase = true)
            matchesStatus && matchesJenis
        }
    }
}
