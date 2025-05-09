package com.example.weatherapp.network.model.login

data class LoginRequest (
    val login : String,    /* login = email */
    val password : String
)