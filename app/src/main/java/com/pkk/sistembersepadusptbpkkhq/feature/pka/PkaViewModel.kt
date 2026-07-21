package com.pkk.sistembersepadusptbpkkhq.feature.pka

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
class PkaViewModel @Inject constructor(
    private val repository: SptbRepository,
    private val authDataStore: AuthDataStore,
) : ViewModel() {

    private val _lawatanList = MutableStateFlow<List<Application>>(emptyList())
    val lawatanList: StateFlow<List<Application>> = _lawatanList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedItem = MutableStateFlow<Application?>(null)
    val selectedItem: StateFlow<Application?> = _selectedItem.asStateFlow()

    private val _updateResult = MutableStateFlow<String?>(null)
    val updateResult: StateFlow<String?> = _updateResult.asStateFlow()

    init {
        loadLawatan()
    }

    fun loadLawatan() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val role = authDataStore.userRole ?: "PKA"
                val name = authDataStore.userName ?: ""
                _lawatanList.value = repository.getApplications(role, name)
            } catch (_: Exception) {
                _lawatanList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateLawatan(rowIndex: Int, tarikh: String, syor: String, ulasanSpi: String) {
        viewModelScope.launch {
            try {
                repository.approveApplication(
                    mapOf(
                        "row_index" to rowIndex.toString(),
                        "lawatan_tarikh" to tarikh,
                        "lawatan_syor" to syor,
                        "ulasan_spi" to ulasanSpi,
                    )
                )
                _updateResult.value = "Lawatan berjaya dikemaskini"
                loadLawatan()
            } catch (_: Exception) {
                _updateResult.value = "Gagal mengemaskini lawatan"
            }
        }
    }

    fun selectItem(item: Application?) {
        _selectedItem.value = item
    }

    fun clearUpdateResult() {
        _updateResult.value = null
    }
}
