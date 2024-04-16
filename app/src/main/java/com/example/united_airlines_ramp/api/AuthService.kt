package com.example.united_airlines_ramp.api

import com.example.united_airlines_ramp.models.LoginRequest
import com.example.united_airlines_ramp.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}