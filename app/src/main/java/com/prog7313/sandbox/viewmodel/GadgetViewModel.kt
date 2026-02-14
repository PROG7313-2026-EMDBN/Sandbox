package com.prog7313.sandbox.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.prog7313.sandbox.model.Gadget
import com.prog7313.sandbox.model.sampleGadgets

class GadgetViewModel : ViewModel() {
    private val _gadgets = MutableStateFlow(sampleGadgets)
    val gadgets: StateFlow<List<Gadget>> = _gadgets.asStateFlow()

    fun addGadget(gadget: Gadget) {
        _gadgets.value = _gadgets.value + gadget
    }
}
