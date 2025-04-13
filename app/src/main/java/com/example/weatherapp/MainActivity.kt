package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.ui.theme.WeatherAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherApp()
        }
    }
}

@Composable
fun WeatherApp() {
    Column(modifier = Modifier.fillMaxHeight().fillMaxWidth().wrapContentSize(Alignment.Center)) {
        Header()
        TodayWeather()
        HourlyWeather()
        DailyWeather()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        WeatherApp()
    }
}