package com.example.mobg6g60505.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobg6g60505.network.AuthRequest
import com.example.mobg6g60505.network.AuthService
import kotlinx.coroutines.launch

class LogInViewModel : ViewModel() {
    var userEmail by mutableStateOf("")
        private set
    var userPassword by mutableStateOf("")
        private set
    var emailError by mutableStateOf<String?>(null)
        private set
    var apiError by mutableStateOf<String?>(null)
        private set
    var loginSuccess by mutableStateOf(false)
        private set

    private val emailRegex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$".toRegex()

    fun updateUserEmail(email: String) {
        userEmail = email
        emailError = null
        apiError = null
    }

    fun updateUserPassword(password: String) {
        userPassword = password
        apiError = null
    }

    fun validateEmail(): Boolean {
        return emailRegex.matches(userEmail)
    }

    fun login() {
        apiError = null

        if (!validateEmail()) {
            emailError = "Format d'email invalide"
            return
        }

        viewModelScope.launch {
            try {
                val response = AuthService.authClient.authenticate(
                    authRequest = AuthRequest(
                        email = userEmail,
                        password = userPassword
                    )
                )

                if (response.isSuccessful) {
                    loginSuccess = true
                } else {
                    apiError = "Email ou mot de passe incorrect"
                    }
            }
            catch (e: Exception) {
                apiError = "Erreur réseau: ${e.localizedMessage}"
            }
        }
    }
}