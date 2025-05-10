package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

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
    var currentScreen   by remember { mutableStateOf<Screen>(Screen.Weather) }
    var currentUser     by remember { mutableStateOf<String?>(null) }
    var welcomeMessage  by remember { mutableStateOf<String?>(null) }
    var showProfileMenu by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val scrollState     = rememberScrollState()
    val welcomeProgress = remember { Animatable(1f) }

    LaunchedEffect(welcomeMessage) {
        if (welcomeMessage != null) {
            welcomeProgress.snapTo(1f)
            welcomeProgress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 5000)
            )
            welcomeMessage = null
        }
    }

    Box(Modifier.fillMaxSize()) {
        // Main content
        when (currentScreen) {
            is Screen.Weather -> {
                Column(
                    Modifier
                        .fillMaxSize()
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
                    Spacer(Modifier.height(24.dp))

                    if (currentUser == null) {
                        Header(
                            onSignUpClick = { currentScreen = Screen.Signup },
                            onLoginClick   = { currentScreen = Screen.Login }
                        )
                    } else {
                        Spacer(Modifier.height(12.dp))
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint               = Color.White,
                                modifier           = Modifier
                                    .size(32.dp)
                                    .clickable { showProfileMenu = !showProfileMenu }
                            )
                        }
                    }

                    TodayWeather()
                    HourlyWeather()
                    DailyWeather()
                }
            }
            is Screen.Login -> {
                LoginScreen(
                    CreateAccountScreen = { currentScreen = Screen.Signup },
                    onSignInSuccess     = { email ->
                        val name = email.substringBefore("@").replaceFirstChar { it.uppercase() }
                        currentUser    = name
                        welcomeMessage = "Welcome, Dear $name"
                        currentScreen  = Screen.Weather
                    },
                    onBack = { currentScreen = Screen.Weather }
                )
            }
            is Screen.Signup -> {
                CreateAccountScreen(
                    LoginScreen      = { currentScreen = Screen.Login },
                    onSignUpSuccess = { email ->
                        val name = email.substringBefore("@").replaceFirstChar { it.uppercase() }
                        currentUser    = name
                        welcomeMessage = "Welcome, Dear $name"
                        currentScreen  = Screen.Weather
                    },
                    onBack = { currentScreen = Screen.Weather }
                )
            }
        }

        // Overlay + Sliding menu
        if (showProfileMenu) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { showProfileMenu = false }
            )
            AnimatedVisibility(
                visible = showProfileMenu,
                enter   = slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(1000, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(1000, easing = FastOutSlowInEasing)),
                exit    = slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(1000, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(1000, easing = FastOutSlowInEasing)),
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(240.dp)
                        .background(Color.LightGray)
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures { _, dragAmount ->
                                if (dragAmount < -20f) showProfileMenu = false
                            }
                        }
                        .padding(start = 16.dp, top = 56.dp)
                ) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Close menu",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { showProfileMenu = false }
                    )
                    Spacer(Modifier.height(24.dp))
                    Image(
                        painter            = painterResource(id = R.drawable.profile_pic),
                        contentDescription = "Profile",
                        contentScale       = ContentScale.Crop,
                        modifier           = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        currentUser.orEmpty(),
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier   = Modifier
                            .padding(vertical = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Divider(Modifier.padding(vertical = 8.dp))
                    Text(
                        "Logout",
                        color      = Color.Red,
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier   = Modifier
                            .clickable {
                                showLogoutDialog = true
                            }
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }

        // Confirm Logout Dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text(text = "Confirm Logout") },
                text = { Text(text = "Are you sure you want to log out?") },
                confirmButton = {
                    TextButton(onClick = {
                        currentUser = null
                        showProfileMenu = false
                        showLogoutDialog = false
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showLogoutDialog = false
                    }) {
                        Text("No")
                    }
                }
            )
        }

        // Animated welcome banner + progress bar
        AnimatedVisibility(
            visible = welcomeMessage != null,
            enter   = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(700, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(700, easing = FastOutSlowInEasing)),
            exit    = fadeOut(animationSpec = tween(700, easing = FastOutSlowInEasing)) +
                    slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(700, easing = FastOutSlowInEasing)),
            modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter)
        ) {
            Column(
                Modifier
                    .padding(top = 32.dp)
                    .background(Color(0xFF4CAF50), RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { welcomeMessage = null }
            ) {
                Text(
                    welcomeMessage.orEmpty(),
                    textAlign  = TextAlign.Center,
                    color      = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp,
                    modifier   = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    Modifier
                        .height(4.dp)
                        .fillMaxWidth(welcomeProgress.value)
                        .background(Color.White)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherAppPreview() {
    MaterialTheme {
        WeatherApp()
    }
}
