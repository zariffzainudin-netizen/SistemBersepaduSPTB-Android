@file:OptIn(ExperimentalMaterial3Api::class)

package com.pkk.sistembersepadusptbpkkhq.feature.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pkk.sistembersepadusptbpkkhq.core.domain.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: AdminViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedUser by viewModel.selectedUser.collectAsState()
    val showAddDialog by viewModel.showAddDialog.collectAsState()
    val showEditDialog by viewModel.showEditDialog.collectAsState()

    var deleteConfirmEmail by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Pengurusan Pengguna",
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.setShowAddDialog(true) },
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Text("+", style = MaterialTheme.typography.headlineMedium, color = Color.White)
            }
        },
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else if (users.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "Tiada pengguna",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(users, key = { it.email }) { user ->
                    UserCard(
                        user = user,
                        onClick = {
                            viewModel.selectUser(user)
                            viewModel.setShowEditDialog(true)
                        },
                        onDelete = { deleteConfirmEmail = user.email },
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        UserFormDialog(
            title = "Tambah Pengguna",
            initial = null,
            onDismiss = { viewModel.setShowAddDialog(false) },
            onConfirm = { name, email, role, phone, color, firebaseCode ->
                viewModel.addUser(name, email, role, phone, color, firebaseCode)
                viewModel.setShowAddDialog(false)
            },
        )
    }

    if (showEditDialog && selectedUser != null) {
        UserFormDialog(
            title = "Sunting Pengguna",
            initial = selectedUser,
            onDismiss = {
                viewModel.setShowEditDialog(false)
                viewModel.selectUser(null)
            },
            onConfirm = { name, email, role, phone, color, firebaseCode ->
                viewModel.updateUser(email, name, role, phone, color, firebaseCode)
                viewModel.setShowEditDialog(false)
                viewModel.selectUser(null)
            },
        )
    }

    deleteConfirmEmail?.let { email ->
        AlertDialog(
            onDismissRequest = { deleteConfirmEmail = null },
            title = { Text("Padam Pengguna") },
            text = { Text("Anda pasti mahu memadam pengguna ini? Tindakan ini tidak boleh dibatalkan.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteUser(email)
                        deleteConfirmEmail = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                ) {
                    Text("Padam")
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteConfirmEmail = null }) {
                    Text("Batal")
                }
            },
        )
    }
}

@Composable
private fun UserCard(
    user: User,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(parseColor(user.color)),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RoleBadge(user.role)
                    Spacer(modifier = Modifier.width(8.dp))
                    if (user.phone.isNotEmpty()) {
                        Text(
                            text = user.phone,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            IconButton(onClick = onDelete) {
                Text("\uD83D\uDDD1\uFE0F", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun RoleBadge(role: String) {
    val color = when (role.uppercase()) {
        "ADMIN" -> Color(0xFF3B82F6)
        "PENGESYOR" -> Color(0xFF10B981)
        "PELULUS" -> Color(0xFF8B5CF6)
        "PKA" -> Color(0xFFF59E0B)
        "KETUA_SEKSYEN" -> Color(0xFFEC4899)
        "PENGARAH" -> Color(0xFFEF4444)
        else -> Color(0xFF64748B)
    }
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.15f),
    ) {
        Text(
            text = role.replace("_", " "),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun UserFormDialog(
    title: String,
    initial: User?,
    onDismiss: () -> Unit,
    onConfirm: (name: String, email: String, role: String, phone: String, color: String, firebaseCode: String) -> Unit,
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var email by remember { mutableStateOf(initial?.email ?: "") }
    var role by remember { mutableStateOf(initial?.role ?: "PENGESYOR") }
    var phone by remember { mutableStateOf(initial?.phone ?: "") }
    var color by remember { mutableStateOf(initial?.color ?: "#2563eb") }
    var firebaseCode by remember { mutableStateOf("") }

    val roles = listOf("PENGESYOR", "PELULUS", "ADMIN", "PKA", "KETUA_SEKSYEN", "PENGARAH")
    val colors = listOf(
        "BIRU" to "#2563eb",
        "OREN" to "#f97316",
        "HIJAU" to "#22c55e",
        "UNGU" to "#8b5cf6",
        "HITAM" to "#000000",
        "PINK" to "#ec4899",
    )
    var roleExpanded by remember { mutableStateOf(false) }
    var colorExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    enabled = initial == null,
                )
                ExposedDropdownMenuBox(
                    expanded = roleExpanded,
                    onExpandedChange = { roleExpanded = it },
                ) {
                    OutlinedTextField(
                        value = role.replace("_", " "),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Peranan") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                    )
                    ExposedDropdownMenu(
                        expanded = roleExpanded,
                        onDismissRequest = { roleExpanded = false },
                    ) {
                        roles.forEach { r ->
                            DropdownMenuItem(
                                text = { Text(r.replace("_", " ")) },
                                onClick = {
                                    role = r
                                    roleExpanded = false
                                },
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Telefon") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                )
                ExposedDropdownMenuBox(
                    expanded = colorExpanded,
                    onExpandedChange = { colorExpanded = it },
                ) {
                    val colorLabel = colors.firstOrNull { it.second == color }?.first ?: "BIRU"
                    OutlinedTextField(
                        value = colorLabel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Warna") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = colorExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                    )
                    ExposedDropdownMenu(
                        expanded = colorExpanded,
                        onDismissRequest = { colorExpanded = false },
                    ) {
                        colors.forEach { (label, hex) ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clip(CircleShape)
                                                .background(parseColor(hex)),
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(label)
                                    }
                                },
                                onClick = {
                                    color = hex
                                    colorExpanded = false
                                },
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = firebaseCode,
                    onValueChange = { firebaseCode = it },
                    label = { Text("Firebase Code") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(name, email, role, phone, color, firebaseCode)
                },
                enabled = name.isNotBlank() && email.isNotBlank(),
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        },
    )
}

private fun parseColor(hex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (_: Exception) {
        Color(0xFF2563EB)
    }
}
