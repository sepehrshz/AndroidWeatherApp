package com.example.weatherapp

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*      // Column, Spacer, fillMaxSize, wrapContentSize, height, width, etc.
import androidx.compose.runtime.*             // @Composable, remember, mutableStateOf, getValue/setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.theme.WeatherAppTheme
import android.os.Bundle


sealed class Screen {
    object Weather : Screen()
    object Login   : Screen()
    object Signup  : Screen()
}

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
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Weather) }

    when (currentScreen) {
        is Screen.Weather -> {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF138EFF),
                                Color(0xFF6BB6FF)
                            )
                        )
                    )
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Header(
                    onSignUpClick = { currentScreen = Screen.Signup },
                    onLoginClick  = { currentScreen = Screen.Login }
                )
                TodayWeather()
                HourlyWeather()
                DailyWeather()
            }
        }
        is Screen.Login -> {
            LoginScreen(
                CreateAccountScreen = { currentScreen = Screen.Signup },
                onSignInSuccess     = { currentScreen = Screen.Weather },
                onBack = { currentScreen = Screen.Weather }
            )
        }
        is Screen.Signup -> {
            CreateAccountScreen(
                LoginScreen = { currentScreen = Screen.Login },
                onSignUpSuccess = { currentScreen = Screen.Weather },
                onBack = { currentScreen = Screen.Weather }

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        WeatherApp()
    }
}