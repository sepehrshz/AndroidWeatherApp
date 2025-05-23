package com.example.weatherapp.network.model.weather

data class WeatherResponse(

    val main : Main,
    val name : String,
    val weather : List<Weather>
)

data class Main(
    val temp : Float,
    val temp_max : Float,
    val temp_min : Float
)

data class Weather(
    val icon : String,
    val description : String
)