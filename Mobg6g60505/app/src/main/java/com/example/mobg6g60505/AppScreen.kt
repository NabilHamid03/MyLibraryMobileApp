package com.example.mobg6g60505

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobg6g60505.ui.LogInViewModel
import com.example.mobg6g60505.ui.screens.LogInScreen
import com.example.mobg6g60505.ui.screens.MainScreen

enum class AppScreen(@StringRes val title: Int) {
    LogIn(title = R.string.app_log_in_title),
    Main(title = R.string.app_main_screen_title)
}

@Composable
fun AppScreen(
    viewModel: LogInViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    Scaffold {
        innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = AppScreen.LogIn.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = AppScreen.LogIn.name) {
                LogInScreen(
                    userEmail = viewModel.userEmail,
                    isEmailWrong = uiState.isEmailWrong,
                    checkEmail = {
                        if(viewModel.checkUserEmail()) {
                            navController.navigate(AppScreen.Main.name)
                        }
                    },
                    onEmailChanged = { viewModel.updateUserEmail(it) },
                )
            }
            composable(route = AppScreen.Main.name) {
                MainScreen(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}
