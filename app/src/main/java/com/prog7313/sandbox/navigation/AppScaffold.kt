package com.prog7313.sandbox.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navController: NavHostController,
    onExit: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val topLevelRoutes = setOf(
        Routes.HOME,
        Routes.GADGETS,
        Routes.FORM
    )

    val isTopLevel = currentRoute in topLevelRoutes

    val canGoBack = navController.previousBackStackEntry != null

    val title = when (currentRoute) {
        Routes.HOME -> "Sandbox"
        Routes.FORM -> "Form"
        Routes.HELLO -> "Welcome"
        Routes.GADGETS -> "Gadgets"
        Routes.ADD_GADGET -> "Add Gadget"
        else -> "Sandbox"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = isTopLevel,
        drawerContent = {
            ModalDrawerSheet() {
                Text("Navigate", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(12.dp))

                Spacer(Modifier.height(12.dp))

                NavigationDrawerItem(
                    label = { Text("Home") },
                    selected = currentRoute == Routes.HOME,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Form") },
                    selected = currentRoute == Routes.FORM,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.FORM) { launchSingleTop = true }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Gadgets") },
                    selected = currentRoute == Routes.GADGETS,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Routes.GADGETS) { launchSingleTop = true }
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(16.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Exit") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onExit()
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(title) },

                    navigationIcon = {
                        if (isTopLevel) {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menu")
                            }
                        } else if (canGoBack) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    },

                    actions = {
                        if (currentRoute == Routes.GADGETS) {
                            IconButton(onClick = { navController.navigate(Routes.ADD_GADGET) }) {
                                Icon(Icons.Filled.Add, contentDescription = "Add gadget")
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            content(Modifier.padding(paddingValues))
        }
    }
}