package com.prog7313.sandbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.prog7313.sandbox.navigation.AppNavGraph
import com.prog7313.sandbox.theme.SandBoxTheme
import com.prog7313.sandbox.viewmodel.SettingsViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.prog7313.sandbox.ui.AuthScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsVm: SettingsViewModel = viewModel()
            val darkMode by settingsVm.darkMode.collectAsState()
            val currentUser = FirebaseAuth.getInstance().currentUser

            SandBoxTheme(darkTheme = darkMode) {
                if (currentUser == null) {
                    AuthScreen(
                        onAuthenticated = { recreate() }
                    )
                } else {
                    AppNavGraph(
                        settingsVm = settingsVm,
                        onExit = { finish() },
                        onLogout = {
                            FirebaseAuth.getInstance().signOut()
                            recreate()
                        }
                    )
                }
            }
        }
    }
}