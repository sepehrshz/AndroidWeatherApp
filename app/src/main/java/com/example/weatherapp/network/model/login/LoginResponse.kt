package com.example.weatherapp.network.model.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("user-token")
    val userToken : String?=null,
    val message : String

)