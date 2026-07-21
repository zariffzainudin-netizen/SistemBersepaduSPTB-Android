package com.pkk.sistembersepadusptbpkkhq.core.domain.repository

import com.pkk.sistembersepadusptbpkkhq.core.database.dao.ApplicationDao
import com.pkk.sistembersepadusptbpkkhq.core.database.dao.InboxDao
import com.pkk.sistembersepadusptbpkkhq.core.database.dao.UserDao
import com.pkk.sistembersepadusptbpkkhq.core.database.entity.ApplicationEntity
import com.pkk.sistembersepadusptbpkkhq.core.database.entity.InboxEntity
import com.pkk.sistembersepadusptbpkkhq.core.database.entity.UserEntity
import com.pkk.sistembersepadusptbpkkhq.core.domain.*
import com.pkk.sistembersepadusptbpkkhq.core.network.ApiRequest
import com.pkk.sistembersepadusptbpkkhq.core.network.SptbApiService
import com.pkk.sistembersepadusptbpkkhq.core.network.UserDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SptbRepositoryImpl @Inject constructor(
    private val api: SptbApiService,
    private val applicationDao: ApplicationDao,
    private val userDao: UserDao,
    private val inboxDao: InboxDao,
) : SptbRepository {

    override suspend fun checkAuth(email: String): User? {
        val response = api.checkAuth(email = email)
        return if (response.isValid && response.user != null) {
            response.user.toDomain()
        } else null
    }

    override suspend fun getApplications(role: String, userName: String): List<Application> {
        val response = api.getData(role = role, userName = userName)
        val apps = response.data.map { it.toApplication() }
        applicationDao.deleteAll()
        applicationDao.insertAll(apps.map { it.toEntity() })
        return apps
    }

    override suspend fun getUsers(): List<User> {
        val response = api.getData()
        val users = response.users.map { it.toUser() }
        userDao.deleteAll()
        userDao.insertAll(users.map { it.toEntity() })
        return users
    }

    override suspend fun getDashboard(role: String, userName: String): DashboardData {
        val response = api.getStats(role = role, userName = userName)
        val stats = response.stats ?: return DashboardData()
        return DashboardData(
            total = stats.total,
            lulus = stats.lulus,
            tolak = stats.tolak,
            proses = stats.proses,
            incompleteDoc = stats.incompleteDoc,
            monthlyTrend = stats.monthly.map { m ->
                MonthlyStat(m.bulan, m.jumlah, m.sokong, m.tidakDisokong, m.proses)
            },
            statusBreakdown = mapOf(
                "LULUS" to stats.lulus,
                "TOLAK" to stats.tolak,
                "PROSES" to stats.proses,
            ),
            typeBreakdown = stats.jenisBreakdown,
            konsultansiBreakdown = stats.konsultansiBreakdown,
            reasonBreakdown = stats.reasonBreakdown,
        )
    }

    override suspend fun getInbox(email: String, role: String): List<InboxMessage> {
        val response = api.postAction(
            ApiRequest(action = "getInbox", email = email, role = role)
        )
        @Suppress("UNCHECKED_CAST")
        val messages = (response.data as? List<Map<String, Any>>)?.map { msg ->
            InboxMessage(
                id = msg["id"] as? String ?: "",
                masa = msg["masa"] as? String ?: "",
                mesej = msg["mesej"] as? String ?: "",
                jenis = msg["jenis"] as? String ?: "",
                role = msg["role"] as? String ?: "",
                dibaca = msg["dibaca"] as? Boolean ?: false,
            )
        } ?: emptyList()
        inboxDao.deleteAll()
        inboxDao.insertAll(messages.map { it.toEntity() })
        return messages
    }

    override suspend fun markInboxRead(email: String, msgId: String) {
        api.postAction(ApiRequest(action = "markInboxRead", email = email, msgId = msgId))
        inboxDao.markRead(msgId)
    }

    override suspend fun deleteInbox(email: String, msgId: String) {
        api.postAction(ApiRequest(action = "deleteInbox", email = email, msgId = msgId))
        inboxDao.deleteByIds(listOf(msgId))
    }

    override suspend fun submitForm(request: Map<String, String>): Boolean {
        val req = ApiRequest(action = "insert", email = request["email"] ?: "")
        val response = api.postAction(req)
        return response.status == "success"
    }

    override suspend fun approveApplication(request: Map<String, String>): Boolean {
        val req = ApiRequest(action = "approve", email = request["email"] ?: "")
        val response = api.postAction(req)
        return response.status == "success"
    }

    override suspend fun extractPdf(type: String, text: String, model: String): Map<String, String> {
        val response = api.extractAI(
            ApiRequest(action = "processAI", type = type, text = text, model = model)
        )
        return if (response.success) response.data else emptyMap()
    }

    override suspend fun createDriveFolder(companyName: String, userName: String, type: String): String {
        val response = api.postAction(
            ApiRequest(
                action = "createDriveFolder",
                syarikat = companyName,
                userName = userName,
                jenis = type,
            )
        )
        return response.data?.toString() ?: ""
    }

    override suspend fun listDriveFiles(folderId: String): List<DriveFile> {
        val response = api.postAction(
            ApiRequest(action = "listDriveFiles", folderId = folderId)
        )
        @Suppress("UNCHECKED_CAST")
        return (response.data as? List<Map<String, String>>)?.map { file ->
            DriveFile(
                id = file["id"] ?: "",
                name = file["name"] ?: "",
                mimeType = file["mimeType"] ?: "",
                size = file["size"]?.toLongOrNull() ?: 0,
                modifiedTime = file["modifiedTime"] ?: "",
            )
        } ?: emptyList()
    }

    override suspend fun addUser(request: Map<String, String>): Boolean {
        val req = ApiRequest(
            action = "addUser",
            userEmail = request["email"] ?: "",
            name = request["name"] ?: "",
            role = request["role"] ?: "",
            phone = request["phone"] ?: "",
            color = request["color"] ?: "",
            firebaseCode = request["firebaseCode"] ?: "",
        )
        val response = api.postAction(req)
        return response.status == "success"
    }

    override suspend fun updateUser(request: Map<String, String>): Boolean {
        val req = ApiRequest(
            action = "updateUser",
            targetEmail = request["email"] ?: "",
            name = request["name"] ?: "",
            role = request["role"] ?: "",
            phone = request["phone"] ?: "",
            color = request["color"] ?: "",
            firebaseCode = request["firebaseCode"] ?: "",
        )
        val response = api.postAction(req)
        return response.status == "success"
    }

    override suspend fun deleteUser(email: String): Boolean {
        val response = api.postAction(
            ApiRequest(action = "deleteUser", targetEmail = email)
        )
        return response.status == "success"
    }
}

// Extension mappers
fun Map<String, String>.toApplication(): Application {
    return Application(
        syarikat = this["syarikat"] ?: "",
        cidb = this["cidb"] ?: "",
        gred = this["gred"] ?: "",
        jenis = this["jenis"] ?: "",
        negeri = this["negeri"] ?: "",
        tarikhSuratTerdahulu = this["tarikh_surat_terdahulu"] ?: "",
        tatatertib = this["tatatertib"] ?: "",
        startDate = this["start_date"] ?: "",
        syorLawatan = this["syor_lawatan"] ?: "",
        dateSubmit = this["date_submit"] ?: "",
        pautan = this["pautan"] ?: "",
        justifikasi = this["justifikasi"] ?: "",
        pengesyor = this["pengesyor"] ?: "",
        syorStatus = this["syor_status"] ?: "",
        tarikhSyor = this["tarikh_syor"] ?: "",
        statusHantarSpi = this["status_hantar_spi"] ?: "",
        tarikhHantarSpi = this["tarikh_hantar_spi"] ?: "",
        lawatanTarikh = this["lawatan_tarikh"] ?: "",
        lawatanSubmitSptb = this["lawatan_submit_sptb"] ?: "",
        lawatanSyor = this["lawatan_syor"] ?: "",
        alamatPerniagaan = this["alamat_perniagaan"] ?: "",
        jenisKonsultansi = this["jenis_konsultansi"] ?: "",
        alasan = this["alasan"] ?: "",
        kelulusan = this["kelulusan"] ?: "",
        tarikhLulus = this["tarikh_lulus"] ?: "",
        pelulus = this["pelulus"] ?: "",
        ubahMaklumat = this["ubah_maklumat"] ?: "",
        ubahGred = this["ubah_gred"] ?: "",
        borangJson = this["borang_json"] ?: "",
        whatsappSchedule = this["whatsapp_schedule"] ?: "",
        inbox = this["inbox"] ?: "",
        ulasanSpi = this["ulasan_spi"] ?: "",
    )
}

fun Map<String, String>.toUser(): User {
    return User(
        name = this["name"] ?: "",
        email = this["email"] ?: "",
        role = this["role"] ?: "",
        color = this["color"] ?: "#2563eb",
        phone = this["phone"] ?: "",
        imageUrl = this["imageUrl"] ?: "",
        signUrl = this["signUrl"] ?: "",
        copUrl = this["copUrl"] ?: "",
    )
}

fun UserDto.toDomain(): User {
    return User(
        name = name,
        email = email,
        role = role,
        color = color,
        phone = phone,
        signUrl = signUrl,
        copUrl = copUrl,
    )
}

fun Application.toEntity(): ApplicationEntity {
    return ApplicationEntity(
        rowIndex = rowIndex,
        syarikat = syarikat,
        cidb = cidb,
        gred = gred,
        jenis = jenis,
        negeri = negeri,
        tarikhSuratTerdahulu = tarikhSuratTerdahulu,
        tatatertib = tatatertib,
        startDate = startDate,
        syorLawatan = syorLawatan,
        dateSubmit = dateSubmit,
        pautan = pautan,
        justifikasi = justifikasi,
        pengesyor = pengesyor,
        syorStatus = syorStatus,
        tarikhSyor = tarikhSyor,
        statusHantarSpi = statusHantarSpi,
        tarikhHantarSpi = tarikhHantarSpi,
        lawatanTarikh = lawatanTarikh,
        lawatanSubmitSptb = lawatanSubmitSptb,
        lawatanSyor = lawatanSyor,
        alamatPerniagaan = alamatPerniagaan,
        jenisKonsultansi = jenisKonsultansi,
        alasan = alasan,
        kelulusan = kelulusan,
        tarikhLulus = tarikhLulus,
        pelulus = pelulus,
        ubahMaklumat = ubahMaklumat,
        ubahGred = ubahGred,
        borangJson = borangJson,
        whatsappSchedule = whatsappSchedule,
        inbox = inbox,
        ulasanSpi = ulasanSpi,
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        email = email,
        name = name,
        role = role,
        color = color,
        phone = phone,
        imageUrl = imageUrl,
        signUrl = signUrl,
        copUrl = copUrl,
    )
}

fun InboxMessage.toEntity(): InboxEntity {
    return InboxEntity(
        id = id,
        masa = masa,
        mesej = mesej,
        jenis = jenis,
        role = role,
        dibaca = dibaca,
    )
}
