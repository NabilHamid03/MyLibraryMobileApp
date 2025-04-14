package com.example.mobg6g60505.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthHTTPClient {
    @POST("auth/v1/token?grant_type=password")
    suspend fun authenticate(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("apikey") apiKey: String = AuthService.apiKey,
        @Body authRequest: AuthRequest
    ): Response<AuthResponse>
}