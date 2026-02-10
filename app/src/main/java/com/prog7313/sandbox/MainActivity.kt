package com.prog7313.sandbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.prog7313.sandbox.navigation.AppNavGraph
import com.prog7313.sandbox.viewmodel.PersonViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val personVm: PersonViewModel = viewModel()

            AppNavGraph(
                personVm = personVm,
                onExit = { finish() }
            )
        }
    }
}