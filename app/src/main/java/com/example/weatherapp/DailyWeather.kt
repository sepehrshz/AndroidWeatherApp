package com.example.weatherapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size           // ← اضافه شد
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale         // ← اضافه شد
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DailyWeather() {
    val dayList = listOf("Tomorrow", "Saturday", "Sunday", "Monday", "Tuesday")

    Column(
        modifier = Modifier
            .padding(start = 16.dp, bottom = 45.dp, end = 16.dp, top = 40.dp)
    ) {
        dayList.forEach { day ->
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
                            .size(48.dp),                 // ← سایز تصویر بزرگتر شد
                        contentScale = ContentScale.Fit   // ← برای حفظ تناسب
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text("18°/19°", color = Color(0xFFFFFEFE), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}
