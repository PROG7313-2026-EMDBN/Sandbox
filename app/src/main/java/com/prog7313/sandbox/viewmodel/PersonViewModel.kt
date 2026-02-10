package com.prog7313.sandbox.viewmodel

import androidx.lifecycle.ViewModel
import com.prog7313.sandbox.model.Person
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PersonViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(Person())
    val uiState: StateFlow<Person> = _uiState

    fun setName(value: String) = _uiState.update { it.copy(name = value) }
    fun setSurname(value: String) = _uiState.update { it.copy(surname = value) }
}