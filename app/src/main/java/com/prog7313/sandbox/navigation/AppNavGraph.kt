package com.prog7313.sandbox.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prog7313.sandbox.ui.FormScreen
import com.prog7313.sandbox.ui.HelloScreen
import com.prog7313.sandbox.viewmodel.PersonViewModel
import com.prog7313.sandbox.model.sampleGadgets
import com.prog7313.sandbox.ui.GadgetsScreen
import com.prog7313.sandbox.ui.HomeScreen

@Composable
fun AppNavGraph(
    personVm: PersonViewModel,
    onExit: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                onOpenForm = { navController.navigate(Routes.FORM) },
                onOpenGadgets = { navController.navigate(Routes.GADGETS) },
                onExit = onExit
            )
        }

        composable(Routes.FORM) {
            FormScreen(
                personVm = personVm,
                onContinue = { navController.navigate(Routes.HELLO) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.HELLO) {
            HelloScreen(
                personVm = personVm,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.GADGETS) {
            GadgetsScreen(
                gadgets = sampleGadgets,
                onBack = { navController.popBackStack() } // goes back to HOME
            )
        }
    }
}
