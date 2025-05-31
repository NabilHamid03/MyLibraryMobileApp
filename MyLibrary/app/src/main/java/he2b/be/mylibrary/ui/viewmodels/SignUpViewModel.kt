package he2b.be.mylibrary.ui.viewmodels

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

class SignUpViewModel : ViewModel() {
    var userEmail = mutableStateOf("")
        private set
    var userPassword = mutableStateOf("")
        private set
    var confirmPassword = mutableStateOf("")
        private set

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _signUpSuccess = MutableStateFlow(false)
    val signUpSuccess = _signUpSuccess.asStateFlow()

    fun updateEmail(newEmail: String) {
        userEmail.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        userPassword.value = newPassword
    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword.value = newConfirmPassword
    }

    fun onSignUpSuccess(navController: NavController) {
        navController.navigate(AppScreenRoutes.Library.name) {
            popUpTo(AppScreenRoutes.SignUp.name) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun onNavigateToLogIn(navController: NavController) {
        navController.navigate(AppScreenRoutes.LogIn.name)
    }

    fun signUp(
        emptyEmailError: String,
        invalidEmailError: String,
        emptyPasswordError: String,
        mismatchPasswordError: String,
        emailExistsMessage: String,
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

        val passwordError = ValidationUtils.validatePassword(
            userPassword.value,
            confirmPassword.value,
            emptyPasswordError,
            mismatchPasswordError
        )
        if (passwordError != null) {
            _errorMessage.value = passwordError
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    Supabase.auth.signUpWith(Email) {
                        email = userEmail.value
                        password = userPassword.value
                    }
                }
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    _signUpSuccess.value = true
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    _errorMessage.value = when {
                        e.message?.contains("user_already_exists", ignoreCase = true) == true -> {
                            emailExistsMessage
                        }
                        else -> unknownErrorMessage
                    }
                }
            }
        }
    }
}