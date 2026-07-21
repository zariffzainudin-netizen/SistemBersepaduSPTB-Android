package com.pkk.sistembersepadusptbpkkhq.feature.inputdb

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

private val jenisOptions = listOf("Baru", "Pembaharuan", "Ubah Maklumat", "Ubah Gred")
private val negeriOptions = listOf(
    "Johor", "Kedah", "Kelantan", "Melaka", "Negeri Sembilan",
    "Pahang", "Perak", "Perlis", "Pulau Pinang", "Sabah",
    "Sarawak", "Selangor", "Terengganu", "W.P. Kuala Lumpur",
    "W.P. Labuan", "W.P. Putrajaya",
)
private val syorStatusOptions = listOf("Sokong", "Tidak Sokong", "Dalam Proses")
private val tatatertibOptions = listOf("Tiada", "Ada")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputDatabaseScreen(
    viewModel: InputDatabaseViewModel = hiltViewModel()
) {
    val isSaving by viewModel.isSaving.collectAsState()
    val saveResult by viewModel.saveResult.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    LaunchedEffect(saveResult) {
        when (saveResult) {
            is SaveResult.Success -> {
                snackbarHostState.showSnackbar("Data berjaya disimpan")
                viewModel.resetForm()
                viewModel.clearResult()
            }
            is SaveResult.Error -> {
                snackbarHostState.showSnackbar((saveResult as SaveResult.Error).message)
                viewModel.clearResult()
            }
            null -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Pangkalan Data") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.syarikat.value,
                onValueChange = { viewModel.syarikat.value = it },
                label = { Text("Syarikat") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = viewModel.cidb.value,
                onValueChange = { viewModel.cidb.value = it },
                label = { Text("CIDB") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = viewModel.gred.value,
                onValueChange = { viewModel.gred.value = it },
                label = { Text("Gred") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            DropdownField(
                value = viewModel.jenis.value,
                onValueChange = { viewModel.jenis.value = it },
                label = "Jenis",
                options = jenisOptions,
                modifier = Modifier.fillMaxWidth(),
            )

            DropdownField(
                value = viewModel.negeri.value,
                onValueChange = { viewModel.negeri.value = it },
                label = "Negeri",
                options = negeriOptions,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = viewModel.tarikhSuratTerdahulu.value,
                onValueChange = { viewModel.tarikhSuratTerdahulu.value = it },
                label = { Text("Tarikh Surat Terdahulu") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            DropdownField(
                value = viewModel.tatatertib.value,
                onValueChange = { viewModel.tatatertib.value = it },
                label = "Tatatertib",
                options = tatatertibOptions,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = viewModel.startDate.value,
                onValueChange = { viewModel.startDate.value = it },
                label = { Text("Tarikh Mula") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = viewModel.syorLawatan.value,
                onValueChange = { viewModel.syorLawatan.value = it },
                label = { Text("Syor Lawatan") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = viewModel.dateSubmit.value,
                onValueChange = { viewModel.dateSubmit.value = it },
                label = { Text("Tarikh Hantar") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = viewModel.pautan.value,
                onValueChange = { viewModel.pautan.value = it },
                label = { Text("Pautan") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = viewModel.justifikasi.value,
                onValueChange = { viewModel.justifikasi.value = it },
                label = { Text("Justifikasi") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )

            OutlinedTextField(
                value = viewModel.pengesyor.value,
                onValueChange = { viewModel.pengesyor.value = it },
                label = { Text("Pengesyor") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            DropdownField(
                value = viewModel.syorStatus.value,
                onValueChange = { viewModel.syorStatus.value = it },
                label = "Syor Status",
                options = syorStatusOptions,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = viewModel.statusHantarSpi.value,
                onValueChange = { viewModel.statusHantarSpi.value = it },
                label = { Text("Status Hantar SPI") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = viewModel.alamatPerniagaan.value,
                onValueChange = { viewModel.alamatPerniagaan.value = it },
                label = { Text("Alamat Perniagaan") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )

            OutlinedTextField(
                value = viewModel.jenisKonsultansi.value,
                onValueChange = { viewModel.jenisKonsultansi.value = it },
                label = { Text("Jenis Konsultansi") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = viewModel.ubahMaklumat.value,
                onValueChange = { viewModel.ubahMaklumat.value = it },
                label = { Text("Ubah Maklumat") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = viewModel.ubahGred.value,
                onValueChange = { viewModel.ubahGred.value = it },
                label = { Text("Ubah Gred") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = viewModel.borangJson.value,
                onValueChange = { viewModel.borangJson.value = it },
                label = { Text("Borang JSON") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.saveToDatabase() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !isSaving,
                shape = RoundedCornerShape(12.dp),
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text("Hantar")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            singleLine = true,
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    },
                )
            }
        }
    }
}
