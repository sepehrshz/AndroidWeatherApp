package com.example.weatherapp

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

sealed class Screen {
    object Weather : Screen()
    object Login : Screen()
    object Signup : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
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
    var welcomeMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    LaunchedEffect(welcomeMessage) {
        if (welcomeMessage != null) {
            delay(5000)
            welcomeMessage = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (currentScreen) {
            is Screen.Weather -> {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF157DE0),
                                    Color(0xFF4A9EF3),
                                    Color(0xFF8ED0FD)
                                )
                            )
                        )
                        .verticalScroll(scrollState)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Header(
                        onSignUpClick = { currentScreen = Screen.Signup },
                        onLoginClick = { currentScreen = Screen.Login }
                    )
                    TodayWeather()
                    HourlyWeather()
                    DailyWeather()
                }
            }
            is Screen.Login -> {
                LoginScreen(
                    CreateAccountScreen = { currentScreen = Screen.Signup },
                    onSignInSuccess = { email: String? ->
                        currentScreen = Screen.Weather
                        email?.let {
                            val username = it.substringBefore("@")
                            welcomeMessage = "Welcome, Dear $username"
                        }
                    },
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

        welcomeMessage?.let { message ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .align(Alignment.TopCenter)
                    .background(Color(0xFF4CAF50), shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                androidx.compose.material3.Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherAppPreview() {
    WeatherApp()
}