package com.pkk.sistembersepadusptbpkkhq.feature.excel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pkk.sistembersepadusptbpkkhq.core.datastore.AuthDataStore
import com.pkk.sistembersepadusptbpkkhq.core.domain.repository.SptbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExcelViewModel @Inject constructor(
    private val repository: SptbRepository,
    private val authDataStore: AuthDataStore,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _exportResult = MutableStateFlow<String?>(null)
    val exportResult: StateFlow<String?> = _exportResult.asStateFlow()

    fun exportToExcel(filtered: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            _exportResult.value = null
            try {
                val role = authDataStore.userRole ?: "PENGESYOR"
                val name = authDataStore.userName ?: ""
                val data = repository.getApplications(role, name)
                val label = if (filtered) "difilter" else "semua"
                _exportResult.value = "Eksport data $label berjaya! (${data.size} rekod)"
            } catch (e: Exception) {
                _exportResult.value = "Eksport gagal: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearResult() {
        _exportResult.value = null
    }
}
