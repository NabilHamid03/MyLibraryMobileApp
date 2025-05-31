package he2b.be.mylibrary.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import he2b.be.mylibrary.AppScreenRoutes
import he2b.be.mylibrary.database.Supabase
import he2b.be.mylibrary.utils.ValidationUtils
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogInViewModel : ViewModel() {
    var userEmail = mutableStateOf("")
        private set
    var userPassword = mutableStateOf("")
        private set

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _logInSuccess = MutableStateFlow(false)
    val logInSuccess = _logInSuccess.asStateFlow()

    fun updateEmail(newEmail: String) {
        userEmail.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        userPassword.value = newPassword
    }

    fun onLoginSuccess(navController: NavController) {
        navController.navigate(AppScreenRoutes.Library.name) {
            popUpTo(AppScreenRoutes.LogIn.name) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun onNavigateToSignUp(navController: NavController) {
        navController.navigate(AppScreenRoutes.SignUp.name)
    }

    fun authenticate(
        emptyEmailError: String,
        invalidEmailError: String,
        missingPasswordMessage: String,
        invalidCredentialsMessage: String,
        unknownErrorMessage: String
    ) {
        val emailError = ValidationUtils.validateEmail(
            userEmail.value,
            emptyEmailError,
            invalidEmailError
        )

        if (emailError != null) {
            _errorMessage.value = emailError
            return
        }

        if (userPassword.value.isEmpty()) {
            _errorMessage.value = missingPasswordMessage
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    Supabase.auth.signInWith(Email) {
                        email = userEmail.value
                        password = userPassword.value
                    }
                }
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    _logInSuccess.value = true
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    Log.e("Authentication", "Full error: ", e)

                    val errorMsg = when {
                        e.message?.contains("invalid_credentials", ignoreCase = true) == true -> {
                            invalidCredentialsMessage
                        }
                        else -> unknownErrorMessage
                    }
                    _errorMessage.value = errorMsg
                }
            }
        }
    }
}