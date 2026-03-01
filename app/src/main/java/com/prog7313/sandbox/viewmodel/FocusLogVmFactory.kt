package com.prog7313.sandbox.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prog7313.sandbox.data.FocusLogRepository
import com.prog7313.sandbox.data.SandboxDb

class FocusLogVmFactory(
    private val appContext: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = SandboxDb.get(appContext)
        val repo = FocusLogRepository(db.focusSessionDao())
        return FocusLogViewModel(repo) as T
    }
}