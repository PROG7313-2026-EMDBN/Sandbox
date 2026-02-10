package com.prog7313.sandbox.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prog7313.sandbox.ui.FormScreen
import com.prog7313.sandbox.ui.HelloScreen
import com.prog7313.sandbox.viewmodel.PersonViewModel

@Composable
fun AppNavGraph(
    personVm: PersonViewModel,
    onExit: () -> Unit
) {
    val navController = rememberNavController()

    NavHost( navController = navController, startDestination = Routes.FORM) {
        composable(Routes.FORM) {
            FormScreen(
                personVm = personVm,
                onContinue = { navController.navigate(Routes.HELLO) },
                onBack = { if (!navController.popBackStack()) onExit() }
            )
        }

        composable(Routes.HELLO) {
            HelloScreen(
                personVm = personVm,
                onBack = { navController.popBackStack() }
            )
        }


    }
}