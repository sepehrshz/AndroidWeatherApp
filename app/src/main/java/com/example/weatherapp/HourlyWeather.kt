package com.example.weatherapp

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.network.Client.WeatherApiClient
import com.example.weatherapp.network.model.weather.ForecastEntry
import java.util.Calendar


@Composable
fun HourlyWeather(
    location : Location?,
    apiKey : String
) {

    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val hourLabels = (1..5).map { i -> (currentHour + i*3) % 24 }

    var forecastList by remember { mutableStateOf<List<ForecastEntry>>(emptyList()) }

    LaunchedEffect(location) {
            try {
                val response = WeatherApiClient.apiService.getHourlyForecast(
                    lat = location?.latitude ?: 32.6311553,
                    lon = location?.longitude ?:51.64102,
                    apiKey = apiKey
                )
                forecastList = response.list.subList(0 , 8)
            }catch (e:Exception){
                e.printStackTrace()
            }


    }

    val hourlyList = listOf(
        Pair("17°" , "Cloudy"),
        Pair("18°" , "Cloudy"),
        Pair("15°" , "Cloudy"),
        Pair("21°" , "Cloudy"),
        Pair("17°" , "Cloudy")
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
       ){
            items(forecastList.size){ index ->
                val item = forecastList[index]
                val hour = item.dt_txt.substring(11 ,16)
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .background(Color.White.copy(alpha = 0.3f) , shape = RoundedCornerShape(
                            topStart = 100.dp,
                            topEnd = 100.dp,
                            bottomStart = 100.dp,
                            bottomEnd = 100.dp
                        ))
                        .padding( vertical = 20.dp,
                                 horizontal = 14.dp)
                        .width(70.dp)
                        .height(170.dp)
                        , verticalArrangement = Arrangement.SpaceBetween
                        ,horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Image(painter = painterResource(id = R.drawable.rainy_weather) , contentDescription = "rainy weather",
                            modifier = Modifier.size(60.dp))
                        Spacer(modifier = Modifier.height(5.dp))
                        Text("${item.main.temp.toInt()}°C" , color = Color.White , fontSize = 32.sp, fontWeight = FontWeight.Bold , textAlign = TextAlign.Center)
                        Text(item.weather[0].description , color = Color.White , fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(17.dp))
                        Text(hour,fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f) , style = MaterialTheme.typography.bodySmall , textAlign = TextAlign.Center)
                  }
            }
        }
}


