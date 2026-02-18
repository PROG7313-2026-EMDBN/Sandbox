package com.prog7313.sandbox.model

import kotlinx.serialization.Serializable

@Serializable
data class Gadget(
    val id: String = "",
    val name: String,
    val brand: String,
    val category: String,
    val price: Double
)