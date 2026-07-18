package com.kuskop.sptb.core.network

import com.google.gson.annotations.SerializedName

// Request/Response DTOs for Apps Script backend
data class ApiRequest(
    val action: String,
    val email: String = "",
    val role: String = "",
    val userName: String = "",
    // Application data
    val syarikat: String = "",
    val cidb: String = "",
    val gred: String = "",
    val jenis: String = "",
    val negeri: String = "",
    val tarikh_surat_terdahulu: String = "",
    val tatatertib: String = "",
    val start_date: String = "",
    val syor_lawatan: String = "",
    val date_submit: String = "",
    val pautan: String = "",
    val justifikasi: String = "",
    val pengesyor: String = "",
    val syor_status: String = "",
    val status_hantar_spi: String = "",
    val alamat_perniagaan: String = "",
    val jenis_konsultansi: String = "",
    val ubah_maklumat: String = "",
    val ubah_gred: String = "",
    val borang_json: String = "",
    val createFolder: Boolean = false,
    val row: Int = 0,
    // Approval
    val kelulusan: String = "",
    val alasan: String = "",
    val catatan: String = "",
    val pelulus: String = "",
    // Admin
    val targetEmail: String = "",
    val name: String = "",
    val userEmail: String = "",
    val phone: String = "",
    val color: String = "",
    val firebaseCode: String = "",
    // PKA
    val lawatan_tarikh: String = "",
    val lawatan_submit_sptb: String = "",
    val lawatan_syor: String = "",
    val ulasan_spi: String = "",
    // WhatsApp
    val mode: String = "",
    val tarikh: String = "",
    val masa: String = "",
    val ayat: String = "",
    // Inbox
    val msgId: String = "",
    // Drive
    val folderId: String = "",
    val fileName: String = "",
    val mimeType: String = "",
    val fileData: String = "",
    val fileId: String = "",
    val newName: String = "",
    // AI
    val type: String = "",
    val text: String = "",
    val model: String = "",
)

data class ApiResponse(
    val status: String = "",
    val data: Any? = null,
    val error: String = "",
)

data class DataResponse(
    val data: List<Map<String, String>> = emptyList(),
    val users: List<Map<String, String>> = emptyList(),
    @SerializedName("deletedLogs") val deletedLogs: List<Map<String, String>> = emptyList(),
    val version: String = "",
)

data class StatsResponse(
    val stats: DashboardDto? = null,
)

data class DashboardDto(
    val total: Int = 0,
    @SerializedName("lulus") val lulus: Int = 0,
    @SerializedName("tolak") val tolak: Int = 0,
    val proses: Int = 0,
    @SerializedName("incompleteDoc") val incompleteDoc: Int = 0,
    val monthly: List<MonthlyDto> = emptyList(),
    val jenisBreakdown: Map<String, Int> = emptyMap(),
    val konsultansiBreakdown: Map<String, Int> = emptyMap(),
    val reasonBreakdown: Map<String, Int> = emptyMap(),
)

data class MonthlyDto(
    val bulan: String = "",
    val jumlah: Int = 0,
    val sokong: Int = 0,
    val tidakDisokong: Int = 0,
    val proses: Int = 0,
)

data class AuthCheckResponse(
    val isValid: Boolean = false,
    val user: UserDto? = null,
    val error: String = "",
)

data class UserDto(
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val color: String = "",
    val phone: String = "",
    val signUrl: String = "",
    val copUrl: String = "",
    val firebaseCode: String = "",
)

data class InboxResponse(
    val messages: List<InboxMessageDto> = emptyList(),
)

data class InboxMessageDto(
    val id: String = "",
    val masa: String = "",
    val mesej: String = "",
    val jenis: String = "",
    val role: String = "",
    val dibaca: Boolean = false,
)

data class AIExtractionResponse(
    val success: Boolean = false,
    val data: Map<String, String> = emptyMap(),
    val error: String = "",
)

data class QueueResponse(
    val siasat: List<QueueItemDto> = emptyList(),
    val pemutihan: List<QueueItemDto> = emptyList(),
)

data class QueueItemDto(
    val syarikat: String = "",
    val cidb: String = "",
    val gred: String = "",
    val pengesyor: String = "",
    val pelulus: String = "",
)
