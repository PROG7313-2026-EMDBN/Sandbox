package com.prog7313.sandbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.prog7313.sandbox.navigation.AppNavGraph
import com.prog7313.sandbox.notifications.NotificationHelper
import com.prog7313.sandbox.theme.SandBoxTheme
import com.prog7313.sandbox.ui.AuthScreen
import com.prog7313.sandbox.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.createChannel(this)

        enableEdgeToEdge()

        setContent {
            val settingsVm: SettingsViewModel = viewModel()
            val darkMode by settingsVm.darkMode.collectAsState()

            val auth = remember { FirebaseAuth.getInstance() }
            var currentUser by remember { mutableStateOf<FirebaseUser?>(auth.currentUser) }

            DisposableEffect(auth) {
                val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                    currentUser = firebaseAuth.currentUser
                }

                auth.addAuthStateListener(listener)

                onDispose {
                    auth.removeAuthStateListener(listener)
                }
            }

            SandBoxTheme(darkTheme = darkMode) {
                if (currentUser == null) {
                    AuthScreen(
                        onAuthenticated = { }
                    )
                } else {
                    AppNavGraph(
                        settingsVm = settingsVm,
                        onExit = { finish() },
                        onLogout = {
                            auth.signOut()
                        }
                    )
                }
            }
        }
    }
}