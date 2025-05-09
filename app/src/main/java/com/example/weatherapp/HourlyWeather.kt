package com.example.weatherapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

@Composable
fun HourlyWeather() {

    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val hourLabels = (1..5).map { i -> (currentHour + i) % 24 }

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
            items(hourlyList.size){ index ->
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

                        ,horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Image(painter = painterResource(id = R.drawable.rainy_weather) , contentDescription = "rainy weather",
                            modifier = Modifier.size(60.dp))
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(hourlyList[index].first , color = Color.White , fontSize = 35.sp, fontWeight = FontWeight.Bold)
                        Text(hourlyList[index].second , color = Color.White , fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(17.dp))
                        Text("${hourLabels[index]}:00",fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f) , style = MaterialTheme.typography.bodySmall)
                  }
            }
        }
}


