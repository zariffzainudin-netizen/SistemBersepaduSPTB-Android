package com.pkk.sistembersepadusptbpkkhq.feature.drive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pkk.sistembersepadusptbpkkhq.core.datastore.AuthDataStore
import com.pkk.sistembersepadusptbpkkhq.core.domain.DriveFile
import com.pkk.sistembersepadusptbpkkhq.core.domain.repository.SptbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriveViewModel @Inject constructor(
    private val repository: SptbRepository,
    private val authDataStore: AuthDataStore,
) : ViewModel() {

    private val _files = MutableStateFlow<List<DriveFile>>(emptyList())
    val files: StateFlow<List<DriveFile>> = _files.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _folderUrl = MutableStateFlow("")
    val folderUrl: StateFlow<String> = _folderUrl.asStateFlow()

    private val _currentFolderName = MutableStateFlow("Root")
    val currentFolderName: StateFlow<String> = _currentFolderName.asStateFlow()

    private val _isCreating = MutableStateFlow(false)
    val isCreating: StateFlow<Boolean> = _isCreating.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun updateFolderUrl(value: String) {
        _folderUrl.value = value
    }

    fun loadFiles(folderId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = repository.listDriveFiles(folderId)
                _files.value = result
                _currentFolderName.value = folderId
            } catch (e: Exception) {
                _error.value = "Gagal memuatkan fail: ${e.message}"
                _files.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createFolder(companyName: String) {
        viewModelScope.launch {
            _isCreating.value = true
            _error.value = null
            try {
                val userName = authDataStore.userName ?: ""
                val role = authDataStore.userRole ?: ""
                val folderId = repository.createDriveFolder(companyName, userName, role)
                _currentFolderName.value = companyName
                loadFiles(folderId)
            } catch (e: Exception) {
                _error.value = "Gagal mencipta folder: ${e.message}"
            } finally {
                _isCreating.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
