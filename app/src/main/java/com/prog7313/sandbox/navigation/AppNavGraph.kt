package com.prog7313.sandbox.navigation

import AddGadgetScreen
import GadgetsScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prog7313.sandbox.model.Gadget
import com.prog7313.sandbox.ui.FormScreen
import com.prog7313.sandbox.ui.HelloScreen
import com.prog7313.sandbox.viewmodel.PersonViewModel
import com.prog7313.sandbox.ui.HomeScreen
import com.prog7313.sandbox.viewmodel.GadgetViewModel

@Composable
fun AppNavGraph(
    onExit: () -> Unit
) {
    val personVm: PersonViewModel = viewModel()
    val gadgetVm: GadgetViewModel = viewModel()

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
                gadgetVm = gadgetVm,
                onBack = { navController.popBackStack() },
                onAdd = { navController.navigate(Routes.ADD_GADGET) }
            )
        }

        composable(Routes.ADD_GADGET) {
            AddGadgetScreen(
                onBack = { navController.popBackStack() },
                onSave = { newGadget: Gadget ->
                    gadgetVm.addGadget(newGadget)
                    navController.popBackStack()
                }
            )
        }
    }
}
