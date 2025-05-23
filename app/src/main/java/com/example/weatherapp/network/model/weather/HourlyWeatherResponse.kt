package com.example.weatherapp.network.model.weather

data class HourlyWeatherResponse (
    val list : List<ForecastEntry>
)

data class ForecastEntry(
    val dt_txt : String,
    val main : Main,
    val weather: List<Weather>

)

