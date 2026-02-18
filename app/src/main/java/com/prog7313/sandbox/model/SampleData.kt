package com.prog7313.sandbox.model

import java.util.UUID

val sampleGadgets = listOf(
    Gadget(id = UUID.randomUUID().toString(), name = "Noise Cancelling Headphones", brand = "Sony", category = "Audio", price = 2999.00),
    Gadget(id = UUID.randomUUID().toString(), name = "Smart Watch", brand = "Samsung", category = "Wearables", price = 5999.00),
    Gadget(id = UUID.randomUUID().toString(), name = "Portable SSD 1TB", brand = "SanDisk", category = "Storage", price = 1899.00),
    Gadget(id = UUID.randomUUID().toString(), name = "Bluetooth Speaker", brand = "JBL", category = "Audio", price = 1499.00)
)
