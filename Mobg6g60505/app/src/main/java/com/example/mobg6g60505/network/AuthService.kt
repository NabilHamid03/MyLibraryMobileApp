package com.example.mobg6g60505.network

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object AuthService {
    private const val baseURL = "https://dnsrivnxleeqdtbyhftv.supabase.co/"
    const val apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRuc3Jpdm54bGVlcWR0YnloZnR2Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTY5NzE0MDI2MSwiZXhwIjoyMDEyNzE2MjYxfQ.jgJ49-c9Z8iPQnLVTnPlfRZpKwyBKht-OY8wMTceSiM"

    val authClient: AuthHTTPClient

    init {
        val moshi = Moshi.Builder()
            .build()

        val jsonConverter = MoshiConverterFactory.create(moshi)

        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(jsonConverter)
            .build()

        authClient = retrofit.create(AuthHTTPClient::class.java)
    }
}