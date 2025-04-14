package com.example.mobg6g60505.network

import com.example.mobg6g60505.data.User

data class AuthResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Int,
    val expires_at: Long,
    val refresh_token: String,
    val user: User
)