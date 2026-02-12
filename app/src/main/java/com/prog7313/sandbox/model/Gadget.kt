package com.prog7313.sandbox.model

import java.util.UUID

data class Gadget(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val brand: String,
    val category: String,
    val price: Double
)