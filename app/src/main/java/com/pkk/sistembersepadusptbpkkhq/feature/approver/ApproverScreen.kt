package com.pkk.sistembersepadusptbpkkhq.feature.approver

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pkk.sistembersepadusptbpkkhq.core.domain.Application

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApproverScreen(
    viewModel: ApproverViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedApplication by viewModel.selectedApplication.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()

    var showApproveDialog by remember { mutableStateOf(false) }
    var showRejectDialog by remember { mutableStateOf(false) }

    if (selectedApplication != null) {
        ApproverDetailContent(
            application = selectedApplication!!,
            isProcessing = isProcessing,
            onBack = { viewModel.clearSelection() },
            onApprove = { showApproveDialog = true },
            onReject = { showRejectDialog = true },
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Pelulus - Permohonan",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(12.dp))

            when (uiState) {
                is ApproverUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ApproverUiState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("\u2705", fontSize = MaterialTheme.typography.displayMedium.fontSize)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Tiada permohonan untuk diluluskan",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                is ApproverUiState.Content -> {
                    val apps = (uiState as ApproverUiState.Content).applications
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(apps) { app ->
                            PendingApplicationCard(
                                application = app,
                                onClick = { viewModel.selectApplication(app) },
                            )
                        }
                    }
                }
            }
        }
    }

    if (showApproveDialog) {
        ReasonDialog(
            title = "Pengesahan Kelulusan",
            confirmLabel = "Luluskan",
            onConfirm = { reason ->
                showApproveDialog = false
                viewModel.approve(reason)
            },
            onDismiss = { showApproveDialog = false },
        )
    }

    if (showRejectDialog) {
        ReasonDialog(
            title = "Pengesahan Penolakan",
            confirmLabel = "Tolak",
            onConfirm = { reason ->
                showRejectDialog = false
                viewModel.reject(reason)
            },
            onDismiss = { showRejectDialog = false },
        )
    }
}

@Composable
private fun PendingApplicationCard(application: Application, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = application.syarikat,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "CIDB: ${application.cidb}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "Gred: ${application.gred}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = application.jenis,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = application.tarikhSyor.ifEmpty { application.dateSubmit },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Pengesyor: ${application.pengesyor}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ApproverDetailContent(
    application: Application,
    isProcessing: Boolean,
    onBack: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Kelulusan Permohonan") },
            navigationIcon = {
                TextButton(onClick = onBack) {
                    Text("\u2190 Kembali")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DetailSection(application)

            Spacer(modifier = Modifier.height(8.dp))

            SyorStatusCard(application)

            Spacer(modifier = Modifier.height(8.dp))

            if (application.justifikasi.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Justifikasi",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = application.justifikasi,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        Surface(
            tonalElevation = 3.dp,
            shadowElevation = 8.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onReject,
                    enabled = !isProcessing,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFEF4444),
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text("Tolak", fontWeight = FontWeight.Bold)
                    }
                }

                Button(
                    onClick = onApprove,
                    enabled = !isProcessing,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF22C55E),
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    } else {
                        Text("Luluskan", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailSection(application: Application) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = application.syarikat,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(12.dp))
            DetailRow("No. CIDB", application.cidb)
            DetailRow("Gred", application.gred)
            DetailRow("Jenis", application.jenis)
            DetailRow("Negeri", application.negeri)
            DetailRow("Alamat", application.alamatPerniagaan)
            DetailRow("Jenis Konsultansi", application.jenisKonsultansi)
            DetailRow("Tarikh Mohon", application.dateSubmit)
            DetailRow("Tatatertib", application.tatatertib)
        }
    }
}

@Composable
private fun SyorStatusCard(application: Application) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Syor Pengesyor",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow("Pengesyor", application.pengesyor)
            DetailRow("Status Syor", application.syorStatus)
            DetailRow("Tarikh Syor", application.tarikhSyor)
            if (application.alasan.isNotEmpty()) {
                DetailRow("Alasan Syor", application.alasan)
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    if (value.isEmpty()) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.35f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(0.65f),
            textAlign = TextAlign.End,
        )
    }
}

@Composable
private fun ReasonDialog(
    title: String,
    confirmLabel: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var reason by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = title, fontWeight = FontWeight.Bold)
        },
        text = {
            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                label = { Text("Alasan / Catatan") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(12.dp),
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(reason) },
                enabled = reason.isNotBlank(),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(confirmLabel)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        },
    )
}
