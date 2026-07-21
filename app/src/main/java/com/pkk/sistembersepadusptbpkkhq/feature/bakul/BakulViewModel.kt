package com.pkk.sistembersepadusptbpkkhq.feature.bakul

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

data class BakulItem(
    val id: String,
    val syarikat: String,
    val cidb: String,
    val gred: String,
    val jenis: String,
)

@HiltViewModel
class BakulViewModel @Inject constructor(
    private val repository: SptbRepository,
    private val authDataStore: AuthDataStore,
) : ViewModel() {

    private val _bakulItems = MutableStateFlow<List<BakulItem>>(emptyList())
    val bakulItems: StateFlow<List<BakulItem>> = _bakulItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadBakul()
    }

    fun loadBakul() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val role = authDataStore.userRole ?: "PENGESYOR"
                val name = authDataStore.userName ?: ""
                val apps = repository.getApplications(role, name)
                _bakulItems.value = apps.map { app ->
                    BakulItem(
                        id = app.id.toString(),
                        syarikat = app.syarikat,
                        cidb = app.cidb,
                        gred = app.gred,
                        jenis = app.jenis,
                    )
                }
            } catch (_: Exception) {
                _bakulItems.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeItem(id: String) {
        _bakulItems.value = _bakulItems.value.filter { it.id != id }
    }

    fun clearBakul() {
        _bakulItems.value = emptyList()
    }
}
