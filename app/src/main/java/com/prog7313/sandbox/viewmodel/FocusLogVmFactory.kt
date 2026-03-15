package com.prog7313.sandbox.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prog7313.sandbox.data.FocusLogRepository
import com.prog7313.sandbox.data.SandboxDb

class FocusLogVmFactory(
    private val appContext: Context,
    private val firebaseUuid: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = SandboxDb.get(appContext)
        val repo = FocusLogRepository(
            dayDao = db.focusDayDao(),
            sessionDao = db.focusSessionDao()
        )
        return FocusLogViewModel(repo, firebaseUuid) as T
    }
}