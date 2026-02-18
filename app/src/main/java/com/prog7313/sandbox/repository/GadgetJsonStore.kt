package com.prog7313.sandbox.data

import android.content.Context
import com.prog7313.sandbox.model.Gadget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.UUID

class GadgetJsonStore(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true; prettyPrint = true }
    private val fileName = "gadgets.json"

    private fun internalFile(): File = File(context.filesDir, fileName)

    private fun ensureSeeded() {
        val f = internalFile()
        if (f.exists()) return

        val seed = context.assets.open(fileName).bufferedReader().use { it.readText() }
        f.writeText(seed)
    }

    suspend fun load(): List<Gadget> = withContext(Dispatchers.IO) {
        ensureSeeded()

        val originalText = internalFile().readText()
        val loaded: List<Gadget> = json.decodeFromString(originalText)

        val normalized = loaded.map { g ->
            if (g.id.isBlank()) g.copy(id = java.util.UUID.randomUUID().toString()) else g
        }

        if (normalized != loaded) {
            internalFile().writeText(json.encodeToString(normalized))
        }
        normalized
    }

    suspend fun save(gadgets: List<Gadget>) = withContext(Dispatchers.IO) {
        ensureSeeded()

        val normalized = gadgets.map { g ->
            if (g.id.isBlank()) g.copy(id = UUID.randomUUID().toString()) else g
        }

        internalFile().writeText(json.encodeToString(normalized))
    }
}