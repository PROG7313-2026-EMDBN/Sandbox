package com.prog7313.sandbox.util

import android.content.Context
import android.content.Intent
import com.prog7313.sandbox.model.Gadget

fun shareGadget(context: Context, gadget: Gadget) {
    val text = """
        Gadget: ${gadget.name}
        Brand: ${gadget.brand}
        Category: ${gadget.category}
        Price: R${"%.2f".format(gadget.price)}
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }

    context.startActivity(Intent.createChooser(intent, "Share gadget"))
}
