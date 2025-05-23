package com.example.weatherapp.network.Service

import com.example.weatherapp.network.model.weather.HourlyWeatherResponse
import com.example.weatherapp.network.model.weather.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("appid") apiKey : String,
        @Query("units") units : String = "metric"
    ): WeatherResponse

    @GET("forecast")
    suspend fun getHourlyForecast(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("appid") apiKey : String,
        @Query("units") units : String = "metric"
    ):HourlyWeatherResponse
}