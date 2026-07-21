package com.pkk.sistembersepadusptbpkkhq.feature.profile

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
class ProfileViewModel @Inject constructor(
    private val repository: SptbRepository,
    private val authDataStore: AuthDataStore,
) : ViewModel() {

    private val _companyName = MutableStateFlow("")
    val companyName: StateFlow<String> = _companyName.asStateFlow()

    private val _cidb = MutableStateFlow("")
    val cidb: StateFlow<String> = _cidb.asStateFlow()

    private val _grade = MutableStateFlow("")
    val grade: StateFlow<String> = _grade.asStateFlow()

    private val _alamatPerniagaan = MutableStateFlow("")
    val alamatPerniagaan: StateFlow<String> = _alamatPerniagaan.asStateFlow()

    private val _alamatSurat = MutableStateFlow("")
    val alamatSurat: StateFlow<String> = _alamatSurat.asStateFlow()

    private val _telefon = MutableStateFlow("")
    val telefon: StateFlow<String> = _telefon.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _saveResult = MutableStateFlow<String?>(null)
    val saveResult: StateFlow<String?> = _saveResult.asStateFlow()

    private val _isProcessingPdf = MutableStateFlow(false)
    val isProcessingPdf: StateFlow<Boolean> = _isProcessingPdf.asStateFlow()

    private val _pdfFileName = MutableStateFlow("")
    val pdfFileName: StateFlow<String> = _pdfFileName.asStateFlow()

    private val _pdfText = MutableStateFlow("")
    val pdfText: StateFlow<String> = _pdfText.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        _companyName.value = authDataStore.userName ?: ""
        _email.value = authDataStore.userEmail ?: ""
    }

    fun updateCompanyName(value: String) { _companyName.value = value }
    fun updateCidb(value: String) { _cidb.value = value }
    fun updateGrade(value: String) { _grade.value = value }
    fun updateAlamatPerniagaan(value: String) { _alamatPerniagaan.value = value }
    fun updateAlamatSurat(value: String) { _alamatSurat.value = value }
    fun updateTelefon(value: String) { _telefon.value = value }
    fun updateEmail(value: String) { _email.value = value }

    fun setPdfFile(name: String, text: String) {
        _pdfFileName.value = name
        _pdfText.value = text
    }

    fun processPdf() {
        viewModelScope.launch {
            _isProcessingPdf.value = true
            try {
                val result = repository.extractPdf("profile", _pdfText.value, "gpt-4o")
                applyExtractedData(result)
            } catch (_: Exception) {
            } finally {
                _isProcessingPdf.value = false
            }
        }
    }

    private fun applyExtractedData(data: Map<String, String>) {
        data["syarikat"]?.let { _companyName.value = it }
        data["cidb"]?.let { _cidb.value = it }
        data["gred"]?.let { _grade.value = it }
        data["alamat_perniagaan"]?.let { _alamatPerniagaan.value = it }
        data["alamat_surat"]?.let { _alamatSurat.value = it }
        data["telefon"]?.let { _telefon.value = it }
        data["email"]?.let { _email.value = it }
    }

    fun saveProfile() {
        viewModelScope.launch {
            _isSaving.value = true
            _saveResult.value = null
            try {
                val request = mapOf(
                    "action" to "save_profile",
                    "syarikat" to _companyName.value,
                    "cidb" to _cidb.value,
                    "gred" to _grade.value,
                    "alamat_perniagaan" to _alamatPerniagaan.value,
                    "alamat_surat" to _alamatSurat.value,
                    "telefon" to _telefon.value,
                    "email" to _email.value,
                    "user" to (authDataStore.userEmail ?: ""),
                )
                val success = repository.submitForm(request)
                _saveResult.value = if (success) "berjaya" else "gagal"
            } catch (_: Exception) {
                _saveResult.value = "gagal"
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun clearSaveResult() {
        _saveResult.value = null
    }
}
