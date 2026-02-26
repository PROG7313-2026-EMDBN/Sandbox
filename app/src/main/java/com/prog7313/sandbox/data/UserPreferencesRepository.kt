package com.prog7313.sandbox.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "sandbox_settings")

class UserPreferencesRepository(private val context: Context) {
    private object Keys {
        val DARK_MODE: Preferences.Key<Boolean> = booleanPreferencesKey("dark_mode")
    }

    val darkModeFlow: Flow<Boolean> =
        context.dataStore.data
            .catch { e ->
                if (e is IOException) emit(androidx.datastore.preferences.core.emptyPreferences())
                else throw e
            }
            .map { prefs ->
                prefs[Keys.DARK_MODE] ?: false
            }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.DARK_MODE] = enabled
        }
    }
}