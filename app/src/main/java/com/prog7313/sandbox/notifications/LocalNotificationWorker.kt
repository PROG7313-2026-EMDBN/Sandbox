package com.prog7313.sandbox.notifications

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class LocalNotificationWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        val title = inputData.getString(KEY_TITLE) ?: "Sandbox Local Notification"
        val message = inputData.getString(KEY_MESSAGE)
            ?: "This notification was created on the device itself."

        NotificationHelper.showNotification(
            context = applicationContext,
            title = title,
            message = message,
            notificationId = 1001
        )

        return Result.success()
    }

    companion object {
        const val KEY_TITLE = "title"
        const val KEY_MESSAGE = "message"
    }
}