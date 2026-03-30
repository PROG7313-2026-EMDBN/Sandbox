package com.prog7313.sandbox.notifications

import android.Manifest
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class SandboxFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_TOKEN", "New token: $token")
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title
            ?: message.data["title"]
            ?: "Sandbox Push Notification"

        val body = message.notification?.body
            ?: message.data["body"]
            ?: "This message came from Firebase Cloud Messaging."

        NotificationHelper.showNotification(
            context = this,
            title = title,
            message = body,
            notificationId = 2001
        )
    }
}