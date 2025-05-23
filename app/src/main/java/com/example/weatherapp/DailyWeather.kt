package com.example.weatherapp

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.network.Client.WeatherApiClient
import com.example.weatherapp.network.model.weather.ForecastEntry
import com.google.android.libraries.places.api.model.kotlin.localDate
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.floor

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyWeather(
    location : Location?,
    apiKey : String
) {

    val today = LocalDate.now()

    val dayList = (1..5).map { offset ->
        val day = today.plusDays(offset.toLong())
        if (offset == 1) "Tomorrow"
        else day.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

    var dailyNonForecast by remember { mutableStateOf<List<ForecastEntry>>(emptyList()) }
    LaunchedEffect(location) {

            try {
                val response = WeatherApiClient.apiService.getHourlyForecast(
                    lat = location?.latitude ?: 32.6311553,
                    lon = location?.longitude ?:51.64102,
                    apiKey = apiKey
                )
                dailyNonForecast = response.list.filter {
                    it.dt_txt.contains("12:00:00")
                }
            }catch (e:Exception){
                e.printStackTrace()
            }

        }

//    val dayList = listOf("Tomorrow", "Saturday", "Sunday", "Monday", "Tuesday")

    Column(
        modifier = Modifier
            .padding(start = 16.dp, bottom = 45.dp, end = 16.dp, top = 40.dp)
    ) {
        dailyNonForecast.forEach { item ->
            val dateTime = LocalDate.parse(item.dt_txt.substring(0,10))
            val day = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL , Locale.getDefault())
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .background(
                        Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = day,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodySmall
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.cloudy_weather),
                        contentDescription = "Cloudy",
                        modifier = Modifier
                            .size(48.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text("${floor(item.main.temp_min).toInt()}°C / ${ceil(item.main.temp_max).toInt()}°C", color = Color(0xFFFFFFFF), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}
