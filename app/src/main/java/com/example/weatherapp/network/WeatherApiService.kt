package com.example.weatherapp.network

import com.example.weatherapp.network.model.weather.WeatherResponse
import okhttp3.Response
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
}