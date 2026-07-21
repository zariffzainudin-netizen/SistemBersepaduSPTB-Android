package com.pkk.sistembersepadusptbpkkhq.feature.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pkk.sistembersepadusptbpkkhq.core.datastore.AuthDataStore
import com.pkk.sistembersepadusptbpkkhq.core.domain.User
import com.pkk.sistembersepadusptbpkkhq.core.domain.repository.SptbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val repository: SptbRepository,
    private val authDataStore: AuthDataStore,
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser: StateFlow<User?> = _selectedUser.asStateFlow()

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    private val _showEditDialog = MutableStateFlow(false)
    val showEditDialog: StateFlow<Boolean> = _showEditDialog.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _users.value = repository.getUsers()
            } catch (_: Exception) {
                _users.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addUser(name: String, email: String, role: String, phone: String, color: String, firebaseCode: String) {
        viewModelScope.launch {
            try {
                repository.addUser(
                    mapOf(
                        "name" to name,
                        "email" to email,
                        "role" to role,
                        "phone" to phone,
                        "color" to color,
                        "firebase_code" to firebaseCode,
                    )
                )
                loadUsers()
            } catch (_: Exception) {
            }
        }
    }

    fun updateUser(email: String, name: String, role: String, phone: String, color: String, firebaseCode: String) {
        viewModelScope.launch {
            try {
                repository.updateUser(
                    mapOf(
                        "email" to email,
                        "name" to name,
                        "role" to role,
                        "phone" to phone,
                        "color" to color,
                        "firebase_code" to firebaseCode,
                    )
                )
                loadUsers()
            } catch (_: Exception) {
            }
        }
    }

    fun deleteUser(email: String) {
        viewModelScope.launch {
            try {
                repository.deleteUser(email)
                loadUsers()
            } catch (_: Exception) {
            }
        }
    }

    fun selectUser(user: User?) {
        _selectedUser.value = user
    }

    fun setShowAddDialog(show: Boolean) {
        _showAddDialog.value = show
    }

    fun setShowEditDialog(show: Boolean) {
        _showEditDialog.value = show
    }
}
