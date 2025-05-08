package com.example.weatherapp.network

import com.example.weatherapp.network.model.SignupRequest
import com.example.weatherapp.network.model.SignupResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/signup")
    fun signup(@Body request: SignupRequest) : Call<SignupResponse>
}