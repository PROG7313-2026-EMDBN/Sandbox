package com.prog7313.sandbox.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prog7313.sandbox.viewmodel.PersonViewModel

@Composable
fun HelloScreen(
    personVm: PersonViewModel,
    onBack: () -> Unit
) {
    val state = personVm.uiState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello, ${state.name} ${state.surname}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(20.dp))

        OutlinedButton(onClick = onBack) {
            Text("Go back")
        }
    }
}