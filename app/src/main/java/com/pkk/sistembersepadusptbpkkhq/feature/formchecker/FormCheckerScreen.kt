package com.pkk.sistembersepadusptbpkkhq.feature.formchecker

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FormCheckerScreen(
    viewModel: FormCheckerViewModel = hiltViewModel()
) {
    val companyName by viewModel.companyName.collectAsState()
    val cidbNumber by viewModel.cidbNumber.collectAsState()
    val grade by viewModel.grade.collectAsState()
    val spkkDuration by viewModel.spkkDuration.collectAsState()
    val stbDuration by viewModel.stbDuration.collectAsState()
    val directors by viewModel.directors.collectAsState()
    val shareholders by viewModel.shareholders.collectAsState()
    val chequeSignatories by viewModel.chequeSignatories.collectAsState()
    val spkkNominees by viewModel.spkkNominees.collectAsState()
    val phoneNumbers by viewModel.phoneNumbers.collectAsState()
    val alamatPerniagaan by viewModel.alamatPerniagaan.collectAsState()
    val alamatSuratMenyurat by viewModel.alamatSuratMenyurat.collectAsState()
    val extractedData by viewModel.extractedData.collectAsState()
    val isProcessingPdf by viewModel.isProcessingPdf.collectAsState()
    val pdfFileName by viewModel.pdfFileName.collectAsState()

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val fileName = it.lastPathSegment ?: "fail.pdf"
            val inputStream = context.contentResolver.openInputStream(it)
            val text = inputStream?.bufferedReader()?.use { reader -> reader.readText() } ?: ""
            viewModel.setPdfFile(fileName, text)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Semak Borang",
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
                Text(
                    text = "Muat Naik PDF",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { filePickerLauncher.launch("application/pdf") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isProcessingPdf,
                ) {
                    if (pdfFileName.isNotEmpty()) {
                        Text("Tukar Fail PDF")
                    } else {
                        Text("Pilih Fail PDF")
                    }
                }

                if (pdfFileName.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = pdfFileName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.processPdf() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isProcessingPdf,
                    ) {
                        if (isProcessingPdf) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Memproses...")
                        } else {
                            Text("Proses PDF")
                        }
                    }
                }
            }
        }

        if (extractedData.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Data Diekstrak",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    extractedData.forEach { (key, value) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = key.replace("_", " ").replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(0.4f),
                            )
                            Text(
                                text = value,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(0.6f),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.applyExtractedData() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text("Guna Data Ini")
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Maklumat Syarikat",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = companyName,
                    onValueChange = { viewModel.updateCompanyName(it) },
                    label = { Text("Nama Syarikat") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = cidbNumber,
                    onValueChange = { viewModel.updateCidbNumber(it) },
                    label = { Text("No. CIDB") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = grade,
                    onValueChange = { viewModel.updateGrade(it) },
                    label = { Text("Gred") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedTextField(
                        value = spkkDuration,
                        onValueChange = { viewModel.updateSpkkDuration(it) },
                        label = { Text("Tempoh SPKK") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = stbDuration,
                        onValueChange = { viewModel.updateStbDuration(it) },
                        label = { Text("Tempoh STB") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Pengarah & Pemegang Saham",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = directors,
                    onValueChange = { viewModel.updateDirectors(it) },
                    label = { Text("Senarai Pengarah (1 setiap baris)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3,
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = shareholders,
                    onValueChange = { viewModel.updateShareholders(it) },
                    label = { Text("Senarai Pemegang Saham (1 setiap baris)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3,
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Penandatangan & Nominee",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = chequeSignatories,
                    onValueChange = { viewModel.updateChequeSignatories(it) },
                    label = { Text("Penandatangan Cek (1 setiap baris)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3,
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = spkkNominees,
                    onValueChange = { viewModel.updateSpkkNominees(it) },
                    label = { Text("Nominee SPKK (1 setiap baris)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3,
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Alamat & Perhubungan",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = phoneNumbers,
                    onValueChange = { viewModel.updatePhoneNumbers(it) },
                    label = { Text("No. Telefon") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = alamatPerniagaan,
                    onValueChange = { viewModel.updateAlamatPerniagaan(it) },
                    label = { Text("Alamat Perniagaan") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3,
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = alamatSuratMenyurat,
                    onValueChange = { viewModel.updateAlamatSuratMenyurat(it) },
                    label = { Text("Alamat Surat-Menyurat") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedButton(
                onClick = { viewModel.clearForm() },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text("Kosongkan")
            }

            if (extractedData.isNotEmpty()) {
                Button(
                    onClick = { viewModel.applyExtractedData() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Guna Data")
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
