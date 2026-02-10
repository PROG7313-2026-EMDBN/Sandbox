package com.prog7313.sandbox.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prog7313.sandbox.viewmodel.PersonViewModel

@Composable
fun FormScreen(
    personVm: PersonViewModel,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    val state = personVm.uiState.collectAsState().value
    val canContinue = state.name.isNotBlank() && state.surname.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = state.name,
            onValueChange = personVm::setName,
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.surname,
            onValueChange = personVm::setSurname,
            label = { Text("Surname") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onContinue,
            enabled = canContinue,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }
}