package com.pkk.sistembersepadusptbpkkhq.feature.lagi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pkk.sistembersepadusptbpkkhq.navigation.NavRoutes

data class FeatureItem(
    val emoji: String,
    val label: String,
    val description: String,
    val route: String,
)

private val features = listOf(
    FeatureItem("\uD83C\uDFE2", "Profil Syarikat", "Pengurusan profil syarikat", NavRoutes.PROFILE),
    FeatureItem("\uD83D\uDCCB", "Semak Borang", "Semak & isi borang permohonan", NavRoutes.FORM_CHECKER),
    FeatureItem("\uD83D\uDDFE\uFE0F", "Pangkalan Data", "Input data ke pangkalan data", NavRoutes.INPUT_DATABASE),
    FeatureItem("\u2705", "Pelulus", "Kelulusan permohonan", NavRoutes.APPROVER_VIEW.replace("{appId}", "0")),
    FeatureItem("\u2699\uFE0F", "Pentadbir", "Pengurusan pengguna & sistem", NavRoutes.ADMIN),
    FeatureItem("\uD83D\uDD0D", "PKA", "Pemantauan Kualiti Air", NavRoutes.PKA),
    FeatureItem("\uD83D\uDCCA", "Excel", "Eksport data ke Excel", NavRoutes.EXCEL),
    FeatureItem("\uD83D\uDED2", "Bakul", "Bakul permohonan", NavRoutes.BAKUL),
    FeatureItem("\uD83D\uDCDC", "Sejarah", "Sejarah permohonan", NavRoutes.HISTORY),
    FeatureItem("\u2699\uFE0F", "Tetapan", "Tetapan aplikasi", NavRoutes.SETTINGS),
    FeatureItem("\u2601\uFE0F", "Drive", "Google Drive integration", NavRoutes.DRIVE),
)

@Composable
fun LagiScreen(onNavigate: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(features) { feature ->
            FeatureCard(
                feature = feature,
                onClick = { onNavigate(feature.route) },
            )
        }
    }
}

@Composable
private fun FeatureCard(feature: FeatureItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(feature.emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = feature.label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = feature.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}
