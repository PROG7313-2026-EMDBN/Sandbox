package com.prog7313.sandbox.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.prog7313.sandbox.model.FocusSession
import com.prog7313.sandbox.viewmodel.FocusLogVmFactory
import com.prog7313.sandbox.viewmodel.FocusLogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusLogScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current.applicationContext
    val vm: FocusLogViewModel = viewModel(factory = FocusLogVmFactory(context))
    val state by vm.uiState.collectAsState()

    var title by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Total Minutes: ${state.totalMinutes}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Session title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = minutes,
                    onValueChange = { minutes = it.filter(Char::isDigit).take(3) },
                    label = { Text("Minutes") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        vm.addSession(title, minutes)
                        title = ""
                        minutes = ""
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Save")
                }
            }
        }

        Text("Sessions", style = MaterialTheme.typography.titleMedium)

        if (state.sessions.isEmpty()) {
            Text("No sessions yet. Add one above.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.sessions, key = { it.id }) { session ->
                    SessionRow(
                        session = session,
                        onDelete = { vm.deleteSession(session) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SessionRow(
    session: FocusSession,
    onDelete: () -> Unit
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(session.title, style = MaterialTheme.typography.titleSmall)
                Text("${session.minutes} min", style = MaterialTheme.typography.bodyMedium)
            }
            TextButton(onClick = onDelete) {
                Text("Delete")
            }
        }
    }
}