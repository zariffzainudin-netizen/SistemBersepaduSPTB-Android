package com.pkk.sistembersepadusptbpkkhq.core.domain

import com.google.gson.annotations.SerializedName

// Sheet1 data structure - 32 columns A-AF
data class Application(
    val id: Long = 0,
    val rowIndex: Int = 0,

    @SerializedName("syarikat") var syarikat: String = "",
    @SerializedName("cidb") var cidb: String = "",
    @SerializedName("gred") var gred: String = "",
    @SerializedName("jenis") var jenis: String = "",
    @SerializedName("negeri") var negeri: String = "",
    @SerializedName("tarikh_surat_terdahulu") var tarikhSuratTerdahulu: String = "",
    @SerializedName("tatatertib") var tatatertib: String = "",
    @SerializedName("start_date") var startDate: String = "",
    @SerializedName("syor_lawatan") var syorLawatan: String = "",
    @SerializedName("date_submit") var dateSubmit: String = "",
    @SerializedName("pautan") var pautan: String = "",
    @SerializedName("justifikasi") var justifikasi: String = "",
    @SerializedName("pengesyor") var pengesyor: String = "",
    @SerializedName("syor_status") var syorStatus: String = "",
    @SerializedName("tarikh_syor") var tarikhSyor: String = "",
    @SerializedName("status_hantar_spi") var statusHantarSpi: String = "",
    @SerializedName("tarikh_hantar_spi") var tarikhHantarSpi: String = "",
    @SerializedName("lawatan_tarikh") var lawatanTarikh: String = "",
    @SerializedName("lawatan_submit_sptb") var lawatanSubmitSptb: String = "",
    @SerializedName("lawatan_syor") var lawatanSyor: String = "",
    @SerializedName("alamat_perniagaan") var alamatPerniagaan: String = "",
    @SerializedName("jenis_konsultansi") var jenisKonsultansi: String = "",
    @SerializedName("alasan") var alasan: String = "",
    @SerializedName("kelulusan") var kelulusan: String = "",
    @SerializedName("tarikh_lulus") var tarikhLulus: String = "",
    @SerializedName("pelulus") var pelulus: String = "",
    @SerializedName("ubah_maklumat") var ubahMaklumat: String = "",
    @SerializedName("ubah_gred") var ubahGred: String = "",
    @SerializedName("borang_json") var borangJson: String = "",
    @SerializedName("whatsapp_schedule") var whatsappSchedule: String = "",
    @SerializedName("inbox") var inbox: String = "",
    @SerializedName("ulasan_spi") var ulasanSpi: String = "",
)

data class User(
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val color: String = "#2563eb",
    val phone: String = "",
    val imageUrl: String = "",
    val signUrl: String = "",
    val copUrl: String = "",
) {
    val isAdmin get() = role.equals("ADMIN", ignoreCase = true)
    val isPengesyor get() = role.equals("PENGESYOR", ignoreCase = true)
    val isPelulus get() = role.equals("PELULUS", ignoreCase = true)
    val isPka get() = role.equals("PKA", ignoreCase = true)
    val isPengarah get() = role.equals("PENGARAH", ignoreCase = true)
    val isKetuaSeksyen get() = role.equals("KETUA_SEKSYEN", ignoreCase = true)
}

data class InboxMessage(
    val id: String = "",
    val masa: String = "",
    val mesej: String = "",
    val jenis: String = "",
    val role: String = "",
    val dibaca: Boolean = false,
)

data class WhatsAppSchedule(
    val mode: String = "",
    val tarikh: String = "",
    val masa: String = "",
    val ayat: String = "",
    val status: String = "",
)

data class DashboardData(
    val total: Int = 0,
    val lulus: Int = 0,
    val tolak: Int = 0,
    val proses: Int = 0,
    val incompleteDoc: Int = 0,
    val monthlyTrend: List<MonthlyStat> = emptyList(),
    val statusBreakdown: Map<String, Int> = emptyMap(),
    val typeBreakdown: Map<String, Int> = emptyMap(),
    val konsultansiBreakdown: Map<String, Int> = emptyMap(),
    val reasonBreakdown: Map<String, Int> = emptyMap(),
)

data class MonthlyStat(
    val bulan: String = "",
    val jumlah: Int = 0,
    val sokong: Int = 0,
    val tidakDisokong: Int = 0,
    val dalamProses: Int = 0,
)

data class Changelog(
    val versi: String = "",
    val tarikh: String = "",
    val penerangan: String = "",
    val imej: String = "",
)

data class QueueItem(
    val syarikat: String = "",
    val cidb: String = "",
    val gred: String = "",
    val pengesyor: String = "",
    val pelulus: String = "",
)

data class DriveFile(
    val id: String = "",
    val name: String = "",
    val mimeType: String = "",
    val size: Long = 0,
    val modifiedTime: String = "",
)

data class FormData(
    val jenisApp: String = "",
    val tarikhMohon: String = "",
    val tatatertib: String = "",
    val justifikasi: String = "",
    val noTelefon: String = "",
    val syarikat: String = "",
    val cidb: String = "",
    val gred: String = "",
    val spkkDuration: String = "",
    val stbDuration: String = "",
    val ssmDate: String = "",
    val ssmStatus: String = "",
    val bankDate: String = "",
    val bankSign: String = "",
    val bankStatus: String = "",
    val personel: List<PersonelItem> = emptyList(),
    val docCarta: String = "",
    val docPeta: String = "",
    val docGambar: String = "",
    val docSewa: String = "",
    val kwsp: List<KwspItem> = emptyList(),
    val tarikhLengkap: String = "",
    val tarikhSiasatan: String = "",
    val tarikhProses: String = "",
    val syorStatus: String = "",
)

data class PersonelItem(
    val nama: String = "",
    val alp: Boolean = false,
    val pe: Boolean = false,
    val tt: Boolean = false,
    val pds: Boolean = false,
    val ic: Boolean = false,
    val sb: Boolean = false,
    val epf: Boolean = false,
)

data class KwspItem(
    val date: String = "",
    val status: String = "",
)
