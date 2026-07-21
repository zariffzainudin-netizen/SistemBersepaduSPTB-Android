@file:OptIn(ExperimentalMaterial3Api::class)

package com.pkk.sistembersepadusptbpkkhq.feature.pka

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pkk.sistembersepadusptbpkkhq.core.domain.Application

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PkaScreen(
    viewModel: PkaViewModel = hiltViewModel()
) {
    val lawatanList by viewModel.lawatanList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedItem by viewModel.selectedItem.collectAsState()
    val updateResult by viewModel.updateResult.collectAsState()

    LaunchedEffect(updateResult) {
        if (updateResult != null) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearUpdateResult()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Pemantauan Kualiti Air",
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            )
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (lawatanList.isEmpty()) {
                Text(
                    "Tiada lawatan",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.Center),
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(lawatanList, key = { it.rowIndex }) { app ->
                        LawatanCard(
                            app = app,
                            isExpanded = selectedItem?.rowIndex == app.rowIndex,
                            onClick = {
                                viewModel.selectItem(
                                    if (selectedItem?.rowIndex == app.rowIndex) null else app
                                )
                            },
                            onSave = { tarikh, syor, ulasan ->
                                viewModel.updateLawatan(app.rowIndex, tarikh, syor, ulasan)
                            },
                        )
                    }
                }
            }

            updateResult?.let { msg ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                ) {
                    Text(msg)
                }
            }
        }
    }
}

@Composable
private fun LawatanCard(
    app: Application,
    isExpanded: Boolean,
    onClick: () -> Unit,
    onSave: (tarikh: String, syor: String, ulasan: String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = app.syarikat,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = "CIDB: ${app.cidb}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "Gred: ${app.gred}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                LawatanStatusChip(app.lawatanSyor)
            }

            if (app.lawatanTarikh.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Lawatan: ${app.lawatanTarikh}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            if (isExpanded) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                LawatanForm(app = app, onSave = onSave)
            }
        }
    }
}

@Composable
private fun LawatanForm(
    app: Application,
    onSave: (tarikh: String, syor: String, ulasan: String) -> Unit,
) {
    var tarikh by remember(app.rowIndex) { mutableStateOf(app.lawatanTarikh) }
    var syor by remember(app.rowIndex) { mutableStateOf(app.lawatanSyor) }
    var ulasan by remember(app.rowIndex) { mutableStateOf(app.ulasanSpi) }
    var syorExpanded by remember { mutableStateOf(false) }

    val syorOptions = listOf("LAWATAN", "TIDAK_LAWATAN", "SIASAT")

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = tarikh,
            onValueChange = { tarikh = it },
            label = { Text("Tarikh Lawatan") },
            placeholder = { Text("YYYY-MM-DD") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
        )
        ExposedDropdownMenuBox(
            expanded = syorExpanded,
            onExpandedChange = { syorExpanded = it },
        ) {
            OutlinedTextField(
                value = syor.replace("_", " "),
                onValueChange = {},
                readOnly = true,
                label = { Text("Syor Lawatan") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = syorExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                shape = RoundedCornerShape(12.dp),
            )
            ExposedDropdownMenu(
                expanded = syorExpanded,
                onDismissRequest = { syorExpanded = false },
            ) {
                syorOptions.forEach { s ->
                    DropdownMenuItem(
                        text = { Text(s.replace("_", " ")) },
                        onClick = {
                            syor = s
                            syorExpanded = false
                        },
                    )
                }
            }
        }
        OutlinedTextField(
            value = ulasan,
            onValueChange = { ulasan = it },
            label = { Text("Ulasan SPI") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            shape = RoundedCornerShape(12.dp),
        )
        Button(
            onClick = { onSave(tarikh, syor, ulasan) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text("Simpan")
        }
    }
}

@Composable
private fun LawatanStatusChip(status: String) {
    val color = when (status.uppercase()) {
        "LAWATAN" -> Color(0xFF22C55E)
        "TIDAK_LAWATAN" -> Color(0xFFEF4444)
        "SIASAT" -> Color(0xFFF59E0B)
        else -> Color(0xFF64748B)
    }
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.15f),
    ) {
        Text(
            text = status.replace("_", " ").ifEmpty { "DRAFT" },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold,
        )
    }
}
