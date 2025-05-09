package com.example.weatherapp.network

import com.example.weatherapp.network.model.signup.SignupRequest
import com.example.weatherapp.network.model.login.LoginRequest

import com.example.weatherapp.network.model.signup.SignupResponse
import com.example.weatherapp.network.model.login.LoginResponse

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("users/register")
    fun signup(@Body request: SignupRequest) : Call<SignupResponse>

    @POST("users/login")
    fun login(@Body request : LoginRequest) : Call<LoginResponse>

}