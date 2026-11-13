package com.example.campuscatalog.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campuscatalog.data.prefs.SortOrder
import com.example.campuscatalog.data.prefs.UserPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    prefs: UserPreferences,
    onDarkModeChange: (Boolean) -> Unit,
    onSortOrderChange: (SortOrder) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dark mode", style = MaterialTheme.typography.titleMedium)
                Switch(checked = prefs.darkMode, onCheckedChange = onDarkModeChange)
            }

            Text(
                "Urutkan berdasarkan",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 24.dp)
            )
            val labels = mapOf(
                SortOrder.TITLE to "Nama (A-Z)",
                SortOrder.NEWEST to "Terbaru",
                SortOrder.CREDITS to "SKS terbanyak"
            )
            SortOrder.entries.forEach { order ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = prefs.sortOrder == order,
                        onClick = { onSortOrderChange(order) }
                    )
                    Text(labels.getValue(order))
                }
            }
        }
    }
}
