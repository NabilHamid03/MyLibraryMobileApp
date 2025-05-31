package he2b.be.mylibrary

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import he2b.be.mylibrary.ui.screens.BookDetailsScreen
import he2b.be.mylibrary.ui.screens.LibraryScreen
import he2b.be.mylibrary.ui.screens.LoginScreen
import he2b.be.mylibrary.ui.screens.SignUpScreen
import he2b.be.mylibrary.ui.viewmodels.BookDetailsViewModel
import he2b.be.mylibrary.ui.viewmodels.LibraryViewModel
import he2b.be.mylibrary.ui.viewmodels.LogInViewModel
import he2b.be.mylibrary.ui.viewmodels.SignUpViewModel

enum class AppScreenRoutes {
    SignUp,
    LogIn,
    Library,
    BookDetails
}

@Composable
fun AppScreen() {
    val navController = rememberNavController()

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreenRoutes.SignUp.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = AppScreenRoutes.SignUp.name) {
                val viewModel: SignUpViewModel = viewModel()
                SignUpScreen(viewModel,navController)
            }
            composable(route = AppScreenRoutes.LogIn.name) {
                val viewModel: LogInViewModel = viewModel()
                LoginScreen(viewModel,navController)
            }
            composable(AppScreenRoutes.Library.name) {
                val viewModel: LibraryViewModel = viewModel()
                LibraryScreen(viewModel,navController)
            }
            composable(
                route = "${AppScreenRoutes.BookDetails.name}/{bookId}",
                arguments = listOf(navArgument("bookId") { type = NavType.StringType })
            ) { backStackEntry ->
                val viewModel: BookDetailsViewModel = viewModel()
                val bookId = backStackEntry.arguments?.getString("bookId") ?: return@composable
                BookDetailsScreen(bookId = bookId, navController = navController, viewModel)
            }

        }
    }
}