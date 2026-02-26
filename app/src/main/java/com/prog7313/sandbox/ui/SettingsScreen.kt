package com.prog7313.sandbox.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prog7313.sandbox.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    settingsVm: SettingsViewModel
) {
    val darkMode by settingsVm.darkMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Appearance", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        Card {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Dark mode", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Toggle between light and dark theme.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Switch(
                    checked = darkMode,
                    onCheckedChange = { settingsVm.setDarkMode(it) }
                )
            }
        }
    }
}