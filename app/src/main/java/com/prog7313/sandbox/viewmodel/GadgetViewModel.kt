package com.prog7313.sandbox.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.prog7313.sandbox.data.GadgetJsonStore
import com.prog7313.sandbox.model.Gadget
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GadgetViewModel(app: Application) : AndroidViewModel(app) {
    private val store = GadgetJsonStore(app.applicationContext)
    private val _gadgets = MutableStateFlow<List<Gadget>>(emptyList())
    val gadgets: StateFlow<List<Gadget>> = _gadgets.asStateFlow()

    init {
        viewModelScope.launch {
            _gadgets.value = store.load()
        }
    }

    fun addGadget(gadget: Gadget) {
        val updated = _gadgets.value + gadget
        _gadgets.value = updated

        viewModelScope.launch {
            store.save(updated)
        }
    }
}
