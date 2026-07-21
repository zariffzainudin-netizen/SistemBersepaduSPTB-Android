package com.pkk.sistembersepadusptbpkkhq.feature.inputdb

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
class InputDatabaseViewModel @Inject constructor(
    private val repository: SptbRepository,
    private val authDataStore: AuthDataStore,
) : ViewModel() {

    var syarikat = MutableStateFlow("")
    var cidb = MutableStateFlow("")
    var gred = MutableStateFlow("")
    var jenis = MutableStateFlow("")
    var negeri = MutableStateFlow("")
    var tarikhSuratTerdahulu = MutableStateFlow("")
    var tatatertib = MutableStateFlow("")
    var startDate = MutableStateFlow("")
    var syorLawatan = MutableStateFlow("")
    var dateSubmit = MutableStateFlow("")
    var pautan = MutableStateFlow("")
    var justifikasi = MutableStateFlow("")
    var pengesyor = MutableStateFlow("")
    var syorStatus = MutableStateFlow("")
    var statusHantarSpi = MutableStateFlow("")
    var alamatPerniagaan = MutableStateFlow("")
    var jenisKonsultansi = MutableStateFlow("")
    var ubahMaklumat = MutableStateFlow("")
    var ubahGred = MutableStateFlow("")
    var borangJson = MutableStateFlow("")

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _saveResult = MutableStateFlow<SaveResult?>(null)
    val saveResult: StateFlow<SaveResult?> = _saveResult.asStateFlow()

    fun saveToDatabase() {
        viewModelScope.launch {
            _isSaving.value = true
            _saveResult.value = null
            try {
                val email = authDataStore.userEmail ?: ""
                val request = linkedMapOf(
                    "action" to "insert",
                    "email" to email,
                    "syarikat" to syarikat.value,
                    "cidb" to cidb.value,
                    "gred" to gred.value,
                    "jenis" to jenis.value,
                    "negeri" to negeri.value,
                    "tarikh_surat_terdahulu" to tarikhSuratTerdahulu.value,
                    "tatatertib" to tatatertib.value,
                    "start_date" to startDate.value,
                    "syor_lawatan" to syorLawatan.value,
                    "date_submit" to dateSubmit.value,
                    "pautan" to pautan.value,
                    "justifikasi" to justifikasi.value,
                    "pengesyor" to pengesyor.value,
                    "syor_status" to syorStatus.value,
                    "status_hantar_spi" to statusHantarSpi.value,
                    "alamat_perniagaan" to alamatPerniagaan.value,
                    "jenis_konsultansi" to jenisKonsultansi.value,
                    "ubah_maklumat" to ubahMaklumat.value,
                    "ubah_gred" to ubahGred.value,
                    "borang_json" to borangJson.value,
                )
                val success = repository.submitForm(request)
                _saveResult.value = if (success) SaveResult.Success else SaveResult.Error("Gagal menyimpan data")
            } catch (e: Exception) {
                _saveResult.value = SaveResult.Error(e.message ?: "Ralat tidak diketahui")
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun resetForm() {
        syarikat.value = ""
        cidb.value = ""
        gred.value = ""
        jenis.value = ""
        negeri.value = ""
        tarikhSuratTerdahulu.value = ""
        tatatertib.value = ""
        startDate.value = ""
        syorLawatan.value = ""
        dateSubmit.value = ""
        pautan.value = ""
        justifikasi.value = ""
        pengesyor.value = ""
        syorStatus.value = ""
        statusHantarSpi.value = ""
        alamatPerniagaan.value = ""
        jenisKonsultansi.value = ""
        ubahMaklumat.value = ""
        ubahGred.value = ""
        borangJson.value = ""
    }

    fun clearResult() {
        _saveResult.value = null
    }
}

sealed class SaveResult {
    data object Success : SaveResult()
    data class Error(val message: String) : SaveResult()
}
