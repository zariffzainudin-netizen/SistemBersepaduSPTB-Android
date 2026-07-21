package com.pkk.sistembersepadusptbpkkhq.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "applications")
data class ApplicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "row_index") val rowIndex: Int = 0,
    @ColumnInfo(name = "syarikat") val syarikat: String = "",
    @ColumnInfo(name = "cidb") val cidb: String = "",
    @ColumnInfo(name = "gred") val gred: String = "",
    @ColumnInfo(name = "jenis") val jenis: String = "",
    @ColumnInfo(name = "negeri") val negeri: String = "",
    @ColumnInfo(name = "tarikh_surat_terdahulu") val tarikhSuratTerdahulu: String = "",
    @ColumnInfo(name = "tatatertib") val tatatertib: String = "",
    @ColumnInfo(name = "start_date") val startDate: String = "",
    @ColumnInfo(name = "syor_lawatan") val syorLawatan: String = "",
    @ColumnInfo(name = "date_submit") val dateSubmit: String = "",
    @ColumnInfo(name = "pautan") val pautan: String = "",
    @ColumnInfo(name = "justifikasi") val justifikasi: String = "",
    @ColumnInfo(name = "pengesyor") val pengesyor: String = "",
    @ColumnInfo(name = "syor_status") val syorStatus: String = "",
    @ColumnInfo(name = "tarikh_syor") val tarikhSyor: String = "",
    @ColumnInfo(name = "status_hantar_spi") val statusHantarSpi: String = "",
    @ColumnInfo(name = "tarikh_hantar_spi") val tarikhHantarSpi: String = "",
    @ColumnInfo(name = "lawatan_tarikh") val lawatanTarikh: String = "",
    @ColumnInfo(name = "lawatan_submit_sptb") val lawatanSubmitSptb: String = "",
    @ColumnInfo(name = "lawatan_syor") val lawatanSyor: String = "",
    @ColumnInfo(name = "alamat_perniagaan") val alamatPerniagaan: String = "",
    @ColumnInfo(name = "jenis_konsultansi") val jenisKonsultansi: String = "",
    @ColumnInfo(name = "alasan") val alasan: String = "",
    @ColumnInfo(name = "kelulusan") val kelulusan: String = "",
    @ColumnInfo(name = "tarikh_lulus") val tarikhLulus: String = "",
    @ColumnInfo(name = "pelulus") val pelulus: String = "",
    @ColumnInfo(name = "ubah_maklumat") val ubahMaklumat: String = "",
    @ColumnInfo(name = "ubah_gred") val ubahGred: String = "",
    @ColumnInfo(name = "borang_json") val borangJson: String = "",
    @ColumnInfo(name = "whatsapp_schedule") val whatsappSchedule: String = "",
    @ColumnInfo(name = "inbox") val inbox: String = "",
    @ColumnInfo(name = "ulasan_spi") val ulasanSpi: String = "",
    @ColumnInfo(name = "cached_at") val cachedAt: Long = System.currentTimeMillis(),
)
