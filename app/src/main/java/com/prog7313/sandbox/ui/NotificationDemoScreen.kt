package com.prog7313.sandbox.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessaging
import com.prog7313.sandbox.notifications.LocalNotificationWorker
import java.util.concurrent.TimeUnit

@Composable
fun NotificationDemoScreen() {
    val context = LocalContext.current

    var generalStatus by remember { mutableStateOf("Ready") }
    var tokenStatus by remember { mutableStateOf("Not loaded") }
    var topicStatus by remember { mutableStateOf("Not subscribed") }
    var isSubscribed by remember { mutableStateOf(false) }

    val isPermissionGranted = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    var permissionGranted by remember { mutableStateOf(isPermissionGranted) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionGranted = granted
        generalStatus = if (granted) {
            "Notification permission granted"
        } else {
            "Notification permission denied"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Notifications Demo",
            style = MaterialTheme.typography.headlineSmall
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Status", style = MaterialTheme.typography.titleMedium)

                Text("Current Status: $generalStatus")

                Text(
                    text = "Permission: ${
                        if (permissionGranted) "Granted" else "Not granted"
                    }"
                )

                Text("Topic: $topicStatus")
                Text("Token Status: $tokenStatus")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Local Notification", style = MaterialTheme.typography.titleMedium)

                Text("Schedules a notification on this device in 10 seconds.")

                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    },
                    enabled = !permissionGranted
                ) {
                    Text(
                        if (permissionGranted) {
                            "Permission Already Granted"
                        } else {
                            "Request Notification Permission"
                        }
                    )
                }

                Button(
                    onClick = {
                        val input = Data.Builder()
                            .putString(
                                LocalNotificationWorker.KEY_TITLE,
                                "Sandbox Local Notification"
                            )
                            .putString(
                                LocalNotificationWorker.KEY_MESSAGE,
                                "This was scheduled using WorkManager."
                            )
                            .build()

                        val request = OneTimeWorkRequestBuilder<LocalNotificationWorker>()
                            .setInitialDelay(10, TimeUnit.SECONDS)
                            .setInputData(input)
                            .build()

                        WorkManager.getInstance(context).enqueue(request)
                        generalStatus = "Local notification scheduled"
                    }
                ) {
                    Text("Schedule Local Notification")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Firebase Cloud Messaging", style = MaterialTheme.typography.titleMedium)

                Text("Subscribe to topic and fetch FCM token.")

                Button(
                    onClick = {
                        if (isSubscribed) {
                            FirebaseMessaging.getInstance()
                                .unsubscribeFromTopic("sandbox")
                                .addOnSuccessListener {
                                    isSubscribed = false
                                    topicStatus = "Not subscribed"
                                    generalStatus = "Unsubscribed from topic"
                                }
                                .addOnFailureListener { e ->
                                    generalStatus = e.message ?: "Error"
                                }
                        } else {
                            FirebaseMessaging.getInstance()
                                .subscribeToTopic("sandbox")
                                .addOnSuccessListener {
                                    isSubscribed = true
                                    topicStatus = "Subscribed to 'sandbox'"
                                    generalStatus = "Topic subscription successful"
                                }
                                .addOnFailureListener { e ->
                                    topicStatus = "Subscription failed"
                                    generalStatus = e.message ?: "Error"
                                }
                        }
                    }
                ) {
                    Text(
                        if (isSubscribed) {
                            "Unsubscribe from Topic"
                        } else {
                            "Subscribe to Topic"
                        }
                    )
                }

                Button(
                    onClick = {
                        FirebaseMessaging.getInstance().token
                            .addOnSuccessListener { token ->
                                tokenStatus = "Token retrieved - check Logcat"
                                generalStatus = "Token retrieved"
                                Log.i("FCM_TOKEN", token)
                            }
                            .addOnFailureListener { e ->
                                tokenStatus = "Failed to load token"
                                generalStatus = e.message ?: "Error"
                            }
                    }
                ) {
                    Text("Get FCM Token")
                }
            }
        }
    }
}