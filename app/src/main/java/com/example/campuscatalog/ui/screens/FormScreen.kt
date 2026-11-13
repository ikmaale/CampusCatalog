package com.example.campuscatalog.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.campuscatalog.data.local.CatalogItemEntity

/**
 * Form untuk tambah (existing == null) atau edit (existing != null).
 * Tidak ada akses DAO: hasil dikirim lewat callback ke ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    existing: CatalogItemEntity?,
    onSave: (title: String, category: String, description: String, credits: Int) -> Unit,
    onBack: () -> Unit
) {
    var title by remember(existing) { mutableStateOf(existing?.title ?: "") }
    var category by remember(existing) { mutableStateOf(existing?.category ?: "") }
    var description by remember(existing) { mutableStateOf(existing?.description ?: "") }
    var credits by remember(existing) { mutableStateOf(existing?.credits?.toString() ?: "") }

    val valid = title.isNotBlank() && category.isNotBlank() && credits.toIntOrNull() != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existing == null) "Tambah Course" else "Edit Course") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = title, onValueChange = { title = it },
                label = { Text("Nama course") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = category, onValueChange = { category = it },
                label = { Text("Kategori") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            OutlinedTextField(
                value = description, onValueChange = { description = it },
                label = { Text("Deskripsi") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            OutlinedTextField(
                value = credits, onValueChange = { credits = it.filter(Char::isDigit) },
                label = { Text("SKS") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            Button(
                onClick = { onSave(title, category, description, credits.toInt()) },
                enabled = valid,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) { Text("Simpan") }
        }
    }
}
