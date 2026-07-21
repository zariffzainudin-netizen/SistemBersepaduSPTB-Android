package com.pkk.sistembersepadusptbpkkhq.feature.formchecker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pkk.sistembersepadusptbpkkhq.core.domain.repository.SptbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormCheckerViewModel @Inject constructor(
    private val repository: SptbRepository,
) : ViewModel() {

    private val _companyName = MutableStateFlow("")
    val companyName: StateFlow<String> = _companyName.asStateFlow()

    private val _cidbNumber = MutableStateFlow("")
    val cidbNumber: StateFlow<String> = _cidbNumber.asStateFlow()

    private val _grade = MutableStateFlow("")
    val grade: StateFlow<String> = _grade.asStateFlow()

    private val _spkkDuration = MutableStateFlow("")
    val spkkDuration: StateFlow<String> = _spkkDuration.asStateFlow()

    private val _stbDuration = MutableStateFlow("")
    val stbDuration: StateFlow<String> = _stbDuration.asStateFlow()

    private val _directors = MutableStateFlow("")
    val directors: StateFlow<String> = _directors.asStateFlow()

    private val _shareholders = MutableStateFlow("")
    val shareholders: StateFlow<String> = _shareholders.asStateFlow()

    private val _chequeSignatories = MutableStateFlow("")
    val chequeSignatories: StateFlow<String> = _chequeSignatories.asStateFlow()

    private val _spkkNominees = MutableStateFlow("")
    val spkkNominees: StateFlow<String> = _spkkNominees.asStateFlow()

    private val _phoneNumbers = MutableStateFlow("")
    val phoneNumbers: StateFlow<String> = _phoneNumbers.asStateFlow()

    private val _alamatPerniagaan = MutableStateFlow("")
    val alamatPerniagaan: StateFlow<String> = _alamatPerniagaan.asStateFlow()

    private val _alamatSuratMenyurat = MutableStateFlow("")
    val alamatSuratMenyurat: StateFlow<String> = _alamatSuratMenyurat.asStateFlow()

    private val _extractedData = MutableStateFlow<Map<String, String>>(emptyMap())
    val extractedData: StateFlow<Map<String, String>> = _extractedData.asStateFlow()

    private val _isProcessingPdf = MutableStateFlow(false)
    val isProcessingPdf: StateFlow<Boolean> = _isProcessingPdf.asStateFlow()

    private val _pdfFileName = MutableStateFlow("")
    val pdfFileName: StateFlow<String> = _pdfFileName.asStateFlow()

    private val _pdfText = MutableStateFlow("")
    val pdfText: StateFlow<String> = _pdfText.asStateFlow()

    fun setPdfFile(name: String, text: String) {
        _pdfFileName.value = name
        _pdfText.value = text
    }

    fun updateCompanyName(value: String) { _companyName.value = value }
    fun updateCidbNumber(value: String) { _cidbNumber.value = value }
    fun updateGrade(value: String) { _grade.value = value }
    fun updateSpkkDuration(value: String) { _spkkDuration.value = value }
    fun updateStbDuration(value: String) { _stbDuration.value = value }
    fun updateDirectors(value: String) { _directors.value = value }
    fun updateShareholders(value: String) { _shareholders.value = value }
    fun updateChequeSignatories(value: String) { _chequeSignatories.value = value }
    fun updateSpkkNominees(value: String) { _spkkNominees.value = value }
    fun updatePhoneNumbers(value: String) { _phoneNumbers.value = value }
    fun updateAlamatPerniagaan(value: String) { _alamatPerniagaan.value = value }
    fun updateAlamatSuratMenyurat(value: String) { _alamatSuratMenyurat.value = value }

    fun processPdf() {
        viewModelScope.launch {
            _isProcessingPdf.value = true
            try {
                val result = repository.extractPdf("form", _pdfText.value, "gpt-4o")
                _extractedData.value = result
            } catch (_: Exception) {
                _extractedData.value = emptyMap()
            } finally {
                _isProcessingPdf.value = false
            }
        }
    }

    fun applyExtractedData() {
        val data = _extractedData.value
        data["syarikat"]?.let { _companyName.value = it }
        data["cidb"]?.let { _cidbNumber.value = it }
        data["gred"]?.let { _grade.value = it }
        data["spkk_tempoh"]?.let { _spkkDuration.value = it }
        data["stb_tempoh"]?.let { _stbDuration.value = it }
        data["pengarah"]?.let { _directors.value = it }
        data["pemegang_saham"]?.let { _shareholders.value = it }
        data["penandatangan_cek"]?.let { _chequeSignatories.value = it }
        data["nominee_spkk"]?.let { _spkkNominees.value = it }
        data["telefon"]?.let { _phoneNumbers.value = it }
        data["alamat_perniagaan"]?.let { _alamatPerniagaan.value = it }
        data["alamat_surat"]?.let { _alamatSuratMenyurat.value = it }
    }

    fun clearForm() {
        _companyName.value = ""
        _cidbNumber.value = ""
        _grade.value = ""
        _spkkDuration.value = ""
        _stbDuration.value = ""
        _directors.value = ""
        _shareholders.value = ""
        _chequeSignatories.value = ""
        _spkkNominees.value = ""
        _phoneNumbers.value = ""
        _alamatPerniagaan.value = ""
        _alamatSuratMenyurat.value = ""
        _extractedData.value = emptyMap()
        _pdfFileName.value = ""
        _pdfText.value = ""
    }
}
