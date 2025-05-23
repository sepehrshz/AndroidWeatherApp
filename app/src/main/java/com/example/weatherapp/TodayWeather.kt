package com.example.weatherapp


import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.weatherapp.network.WeatherApiClient
import com.example.weatherapp.network.model.weather.WeatherResponse

@Composable
fun TodayWeather(
    location : Location?,
    apiKey : String
) {

    var temp  by remember { mutableStateOf<String?>(null) }
    var city by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(location) {
        if(location != null){
            try{
                val response = WeatherApiClient.apiService.getCurrentWeather(
                    lat = location.latitude,
                    lon = location.longitude,
                    apiKey = apiKey
                )
                temp = "${response.main.temp.toInt()}"
                city = response.name

            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 1.dp, bottom = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Temperature and Location
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "${temp?:"--"}°C",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Box(
                    modifier = Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.location),
                            contentDescription = "Location Icon",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = city?: "Loading...",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Weather Icon
            Image(
                painter = painterResource(id = R.drawable.sunny_weather),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(220.dp)
            )
        }
    }
}

