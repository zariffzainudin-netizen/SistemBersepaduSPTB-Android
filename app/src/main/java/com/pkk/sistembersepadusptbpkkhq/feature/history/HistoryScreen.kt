package com.pkk.sistembersepadusptbpkkhq.feature.history

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pkk.sistembersepadusptbpkkhq.core.domain.Application

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val filteredItems by viewModel.filteredItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val statusFilter by viewModel.statusFilter.collectAsState()
    val jenisFilter by viewModel.jenisFilter.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sejarah Permohonan") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = isLoading,
            onRefresh = { viewModel.loadHistory() },
            modifier = Modifier.fillMaxSize().padding(padding),
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                FilterRow(
                    statusFilter = statusFilter,
                    jenisFilter = jenisFilter,
                    statusOptions = viewModel.statusOptions,
                    jenisOptions = viewModel.jenisOptions,
                    onStatusChange = { viewModel.setStatusFilter(it) },
                    onJenisChange = { viewModel.setJenisFilter(it) },
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (isLoading && filteredItems.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (filteredItems.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("\uD83D\uDCC2", fontSize = MaterialTheme.typography.displayMedium.fontSize)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Tiada rekod permohonan",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(filteredItems, key = { it.id }) { app ->
                            HistoryItemCard(app)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterRow(
    statusFilter: String,
    jenisFilter: String,
    statusOptions: List<String>,
    jenisOptions: List<String>,
    onStatusChange: (String) -> Unit,
    onJenisChange: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Tapis", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilterChip(
                selected = statusFilter == "Semua",
                onClick = { onStatusChange("Semua") },
                label = { Text("Semua") },
            )
            statusOptions.drop(1).forEach { option ->
                FilterChip(
                    selected = statusFilter == option,
                    onClick = { onStatusChange(option) },
                    label = { Text(option) },
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            jenisOptions.forEach { option ->
                FilterChip(
                    selected = jenisFilter == option,
                    onClick = { onJenisChange(option) },
                    label = { Text(option) },
                )
            }
        }
    }
}

@Composable
private fun HistoryItemCard(app: Application) {
    val statusColor = when (app.kelulusan.lowercase()) {
        "lulus" -> Color(0xFF22C55E)
        "tolak" -> Color(0xFFEF4444)
        else -> Color(0xFFF59E0B)
    }
    val statusBg = when (app.kelulusan.lowercase()) {
        "lulus" -> Color(0xFF22C55E).copy(alpha = 0.1f)
        "tolak" -> Color(0xFFEF4444).copy(alpha = 0.1f)
        else -> Color(0xFFF59E0B).copy(alpha = 0.1f)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = app.syarikat,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusBg,
                ) {
                    Text(
                        text = app.kelulusan.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                InfoChip("CIDB", app.cidb)
                InfoChip("Gred", app.gred)
                InfoChip("Jenis", app.jenis)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = app.tarikhLulus.ifEmpty { app.tarikhSyor },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun InfoChip(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value.ifEmpty { "-" },
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
