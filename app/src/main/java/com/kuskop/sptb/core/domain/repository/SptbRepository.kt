package com.kuskop.sptb.core.domain.repository

import com.kuskop.sptb.core.domain.*

interface SptbRepository {
    // Auth
    suspend fun checkAuth(email: String): User?

    // Data
    suspend fun getApplications(role: String, userName: String): List<Application>
    suspend fun getUsers(): List<User>
    suspend fun getDashboard(role: String, userName: String): DashboardData

    // Inbox
    suspend fun getInbox(email: String, role: String): List<InboxMessage>
    suspend fun markInboxRead(email: String, msgId: String)
    suspend fun deleteInbox(email: String, msgId: String)

    // Submit
    suspend fun submitForm(request: Map<String, String>): Boolean
    suspend fun approveApplication(request: Map<String, String>): Boolean

    // AI
    suspend fun extractPdf(type: String, text: String, model: String): Map<String, String>

    // Drive
    suspend fun createDriveFolder(companyName: String, userName: String, type: String): String
    suspend fun listDriveFiles(folderId: String): List<DriveFile>

    // Admin
    suspend fun addUser(request: Map<String, String>): Boolean
    suspend fun updateUser(request: Map<String, String>): Boolean
    suspend fun deleteUser(email: String): Boolean
}
