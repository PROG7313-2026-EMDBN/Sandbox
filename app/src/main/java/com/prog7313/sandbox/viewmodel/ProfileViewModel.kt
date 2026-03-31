package com.prog7313.sandbox.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import com.prog7313.sandbox.data.ProfileRepository
import com.prog7313.sandbox.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ProfileRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var listener: ValueEventListener? = null

    init {
        loadProfile()
    }

    fun onDisplayNameChange(value: String) {
        _uiState.value = _uiState.value.copy(
            displayName = value,
            error = null,
            message = null
        )
    }

    fun onBadgeTitleChange(value: String) {
        _uiState.value = _uiState.value.copy(
            badgeTitle = value,
            error = null,
            message = null
        )
    }

    private fun loadProfile() {
        val user = auth.currentUser
        if (user == null) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "No logged in user"
            )
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = false)

        viewModelScope.launch {
            try {
                repo.ensureProfileExists(user.uid, user.email.orEmpty())

                listener = repo.observeProfile(user.uid) { profile ->
                    if (profile != null) {
                        _uiState.value = _uiState.value.copy(
                            email = profile.email,
                            displayName = profile.displayName,
                            badgeTitle = profile.badgeTitle,
                            avatarUrl = profile.avatarUrl
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Could not load profile"
                )
            }
        }
    }

    fun saveProfile() {
        val user = auth.currentUser ?: return

        viewModelScope.launch {
            try {
                val profile = UserProfile(
                    uid = user.uid,
                    email = _uiState.value.email,
                    displayName = _uiState.value.displayName.trim(),
                    badgeTitle = _uiState.value.badgeTitle.trim(),
                    avatarUrl = _uiState.value.avatarUrl
                )

                repo.saveProfile(profile)

                _uiState.value = _uiState.value.copy(
                    message = "Profile saved",
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Save failed"
                )
            }
        }
    }

    override fun onCleared() {
        val user = auth.currentUser
        val currentListener = listener
        if (user != null && currentListener != null) {
            repo.removeListener(user.uid, currentListener)
        }
        super.onCleared()
    }
}