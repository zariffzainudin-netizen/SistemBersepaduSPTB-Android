package com.pkk.sistembersepadusptbpkkhq.feature.drive

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DriveScreen(
    viewModel: DriveViewModel = hiltViewModel()
) {
    val files by viewModel.files.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val folderUrl by viewModel.folderUrl.collectAsState()
    val currentFolderName by viewModel.currentFolderName.collectAsState()
    val isCreating by viewModel.isCreating.collectAsState()
    val error by viewModel.error.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var companyNameInput by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Cipta Folder Baru") },
            text = {
                OutlinedTextField(
                    value = companyNameInput,
                    onValueChange = { companyNameInput = it },
                    label = { Text("Nama Syarikat") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (companyNameInput.isNotBlank()) {
                            viewModel.createFolder(companyNameInput.trim())
                            companyNameInput = ""
                            showCreateDialog = false
                        }
                    },
                    enabled = companyNameInput.isNotBlank() && !isCreating,
                ) {
                    if (isCreating) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Cipta")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Batal")
                }
            },
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Google Drive",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = folderUrl,
                        onValueChange = { viewModel.updateFolderUrl(it) },
                        label = { Text("Folder ID / URL") },
                        placeholder = { Text("Masukkan ID folder atau URL Drive") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Button(
                            onClick = {
                                val id = folderUrl.trim()
                                if (id.isNotBlank()) viewModel.loadFiles(id)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            enabled = folderUrl.isNotBlank() && !isLoading,
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                            } else {
                                Text("Buka Folder")
                            }
                        }
                        OutlinedButton(
                            onClick = { showCreateDialog = true },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Text("Cipta Folder Baru")
                        }
                    }
                }
            }

            if (files.isNotEmpty()) {
                Text(
                    text = "Fail dalam $currentFolderName",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            if (isLoading && files.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (files.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("\u2601\uFE0F", fontSize = MaterialTheme.typography.displayMedium.fontSize)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Tiada fail dalam folder ini",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Buka folder atau cipta folder baru",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    items(files, key = { it.id }) { file ->
                        DriveFileItem(file)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DriveFileItem(file: com.pkk.sistembersepadusptbpkkhq.core.domain.DriveFile) {
    val icon = when {
        file.mimeType.contains("folder") -> "\uD83D\uDCC1"
        file.mimeType.contains("pdf") -> "\uD83D\uDCC4"
        file.mimeType.contains("image") -> "\uD83D\uDDBC\uFE0F"
        file.mimeType.contains("spreadsheet") || file.mimeType.contains("excel") -> "\uD83D\uDCCA"
        file.mimeType.contains("document") || file.mimeType.contains("word") -> "\uD83D\uDCDD"
        file.mimeType.contains("presentation") || file.mimeType.contains("powerpoint") -> "\uD83D\uDCCA"
        else -> "\uD83D\uDCC4"
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { /* open file */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(icon, fontSize = MaterialTheme.typography.titleLarge.fontSize)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = file.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (file.modifiedTime.isNotEmpty()) {
                    Text(
                        text = file.modifiedTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            if (file.size > 0) {
                Text(
                    text = formatFileSize(file.size),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        else -> "%.1f MB".format(bytes.toDouble() / (1024 * 1024))
    }
}
