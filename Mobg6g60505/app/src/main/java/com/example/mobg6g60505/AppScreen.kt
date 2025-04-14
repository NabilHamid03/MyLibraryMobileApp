package com.example.mobg6g60505

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobg6g60505.ui.LogInViewModel
import com.example.mobg6g60505.ui.screens.AboutScreen
import com.example.mobg6g60505.ui.screens.LogInScreen
import com.example.mobg6g60505.ui.screens.MainScreen
import androidx.navigation.compose.currentBackStackEntryAsState


enum class AppScreen {
    LogIn,
    Main,
    About
}

@Composable
fun AppScreen(
    viewModel: LogInViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute == AppScreen.Main.name || currentRoute == AppScreen.About.name

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.LogIn.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = AppScreen.LogIn.name) {
                LogInScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable(route = AppScreen.Main.name) {
                MainScreen(modifier = Modifier.fillMaxSize())
            }
            composable(route = AppScreen.About.name) {
                AboutScreen()
            }
        }
    }
}

@Composable
fun AppBottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Accueil") },
            label = { Text("Accueil") },
            selected = currentRoute == AppScreen.Main.name,
            onClick = {
                navController.navigate(AppScreen.Main.name) {
                    popUpTo(AppScreen.Main.name) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Info, contentDescription = "À propos") },
            label = { Text("À propos") },
            selected = currentRoute == AppScreen.About.name,
            onClick = {
                navController.navigate(AppScreen.About.name) {
                    popUpTo(AppScreen.About.name) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }
}
