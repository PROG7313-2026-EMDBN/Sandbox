package com.prog7313.sandbox.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.prog7313.sandbox.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    vm: ProfileViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri.value = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    selectedImageUri.value != null -> {
                        Image(
                            painter = rememberAsyncImagePainter(selectedImageUri.value),
                            contentDescription = "Selected profile image",
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    state.avatarUrl.isNotBlank() -> {
                        AsyncImage(
                            model = state.avatarUrl,
                            contentDescription = "Profile image",
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    else -> {
                        Spacer(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = state.email.ifBlank { "No email" },
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { imagePicker.launch("image/*") },
                    enabled = !state.isSaving
                ) {
                    Text("Choose Profile Image")
                }
            }
        }

        OutlinedTextField(
            value = state.displayName,
            onValueChange = vm::onDisplayNameChange,
            label = { Text("Display name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !state.isSaving
        )

        OutlinedTextField(
            value = state.badgeTitle,
            onValueChange = vm::onBadgeTitleChange,
            label = { Text("Badge title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !state.isSaving
        )

        state.message?.let {
            Text(it, color = MaterialTheme.colorScheme.primary)
        }

        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = { vm.saveProfile(selectedImageUri.value) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isSaving
        ) {
            if (state.isSaving) {
                CircularProgressIndicator()
            } else {
                Text("Save Profile")
            }
        }
    }
}