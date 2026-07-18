package com.kuskop.sptb.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuskop.sptb.core.datastore.AuthDataStore
import com.kuskop.sptb.core.domain.DashboardData
import com.kuskop.sptb.core.domain.repository.SptbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: SptbRepository,
    private val authDataStore: AuthDataStore,
) : ViewModel() {

    private val _dashboardData = MutableStateFlow(DashboardData())
    val dashboardData: StateFlow<DashboardData> = _dashboardData.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val role = authDataStore.userRole ?: "PENGESYOR"
                val name = authDataStore.userName ?: ""
                val data = repository.getDashboard(role, name)
                _dashboardData.value = data
            } catch (e: Exception) {
                _dashboardData.value = DashboardData()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
