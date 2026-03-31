package com.prog7313.sandbox.viewmodel

data class ProfileUiState(
    val email: String = "",
    val displayName: String = "",
    val badgeTitle: String = "",
    val avatarUrl: String = "",
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null
)