package com.prog7313.sandbox.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onOpenForm: () -> Unit,
    onOpenGadgets: () -> Unit,
    onExit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sandbox Home", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onOpenForm,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Open Form") }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onOpenGadgets,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Open Gadgets") }

        Spacer(Modifier.height(24.dp))

        OutlinedButton(
            onClick = onExit,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Exit") }
    }
}
