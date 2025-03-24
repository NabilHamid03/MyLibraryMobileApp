package com.example.mobg6g60505.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mobg6g60505.data.LogInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LogInViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LogInState())
    val uiState: StateFlow<LogInState> = _uiState.asStateFlow()

    var userEmail by mutableStateOf("")
        private set

    fun updateUserEmail(email: String){
        userEmail = email
    }

    fun checkUserEmail(): Boolean {
        val regex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$".toRegex()
        if (regex.matches(userEmail)) {
            _uiState.update { currentState ->
                currentState.copy(
                    isEmailWrong = false,
                    userEmail = userEmail
                )
            }
            return true
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isEmailWrong = true,
                    userEmail = ""
                )
            }
            return false
        }
    }
}