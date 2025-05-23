package com.example.weatherapp

import android.app.Activity
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
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
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.util.LocationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

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
    val context = LocalContext.current
    val activity = context as? Activity

    var location by remember { mutableStateOf<Location?>(null) }

    LaunchedEffect(Unit) {

        activity?.let {
            val helper = LocationHelper(it)
            location = helper.getLastKnownLocation()
        }
    }


    var userToken by remember { mutableStateOf<String?>(null) }
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Weather) }
    var currentUser by remember { mutableStateOf<String?>(null) }
    var welcomeMessage by remember { mutableStateOf<String?>(null) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val welcomeProgress = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    // Drawer state and animation variables
    val drawerWidth = 240.dp
    val drawerWidthPx = with(LocalDensity.current) { drawerWidth.toPx() }
    val drawerOffsetX = remember { Animatable(-drawerWidthPx) }

    // Calculate drawer visibility state based on position
    val drawerVisibilityThreshold = drawerWidthPx * 0.1f
    val isDrawerVisible = remember { derivedStateOf { drawerOffsetX.value > -drawerWidthPx + drawerVisibilityThreshold } }

    // Function to update drawer position
    fun updateDrawerPosition(targetValue: Float, velocity: Float = Float.POSITIVE_INFINITY) {
        scope.launch {
            val target = targetValue.coerceIn(-drawerWidthPx, 0f)
            drawerOffsetX.animateTo(
                targetValue = target,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow,
                    visibilityThreshold = 0.5f
                ),
                initialVelocity = velocity
            )
        }
    }

    // Function to toggle drawer
    fun toggleDrawer() {
        updateDrawerPosition(
            if (isDrawerVisible.value) -drawerWidthPx else 0f
        )
    }

    // Function to open drawer partially
    fun openDrawerPartially(offsetX: Float) {
        val normalizedOffset = (offsetX / drawerWidthPx).coerceIn(0f, 1f)
        val targetOffset = -drawerWidthPx + (normalizedOffset * drawerWidthPx)
        scope.launch {
            drawerOffsetX.snapTo(targetOffset)
        }
    }

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

    Box(
        Modifier
            .fillMaxSize()
            // Edge drag detection for opening drawer
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { offset ->
                        // Only start drag from left edge (for opening)
                        if (offset.x < 50 && !isDrawerVisible.value) {
                            openDrawerPartially(0f) // Start drawer animation
                        }
                    },
                    onDragEnd = {
                        // Snap to closest position
                        if (drawerOffsetX.value > -drawerWidthPx / 2) {
                            updateDrawerPosition(0f) // Fully open
                        } else {
                            updateDrawerPosition(-drawerWidthPx) // Fully closed
                        }
                    },
                    onDragCancel = {
                        // Snap to closest position
                        if (drawerOffsetX.value > -drawerWidthPx / 2) {
                            updateDrawerPosition(0f) // Fully open
                        } else {
                            updateDrawerPosition(-drawerWidthPx) // Fully closed
                        }
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        if (isDrawerVisible.value || change.position.x < 50) {
                            change.consumePositionChange()
                            scope.launch {
                                drawerOffsetX.snapTo(
                                    (drawerOffsetX.value + dragAmount).coerceIn(-drawerWidthPx, 0f)
                                )
                            }
                        }
                    }
                )
            }
    ) {
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
                                    .clickable { toggleDrawer() }
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
                    onSignInSuccess     = { email:String  ->
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

        // Semi-transparent overlay when drawer is visible
        val overlayAlpha by animateFloatAsState(
            targetValue = if (isDrawerVisible.value) 0.5f else 0f,
            animationSpec = tween(durationMillis = 300),
            label = "overlayAlpha"
        )

        if (overlayAlpha > 0) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = overlayAlpha))
                    .clickable(enabled = isDrawerVisible.value) {
                        updateDrawerPosition(-drawerWidthPx)
                    }
            )
        }

        // Drawer content with smooth position animation
        Box(
            modifier = Modifier
                .offset { IntOffset(drawerOffsetX.value.roundToInt(), 0) }
                .width(drawerWidth)
                .fillMaxHeight()
                .background(Color.LightGray.copy(alpha = 0.9f))
                // Drawer-specific drag handling
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            // Snap to closest position
                            if (drawerOffsetX.value > -drawerWidthPx / 2) {
                                updateDrawerPosition(0f) // Fully open
                            } else {
                                updateDrawerPosition(-drawerWidthPx) // Fully closed
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consumePositionChange()
                            scope.launch {
                                drawerOffsetX.snapTo(
                                    (drawerOffsetX.value + dragAmount).coerceIn(-drawerWidthPx, 0f)
                                )
                            }
                        }
                    )
                }
        ) {
            if (isDrawerVisible.value) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 16.dp, top = 56.dp)
                ) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Close menu",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { updateDrawerPosition(-drawerWidthPx) }
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
                        updateDrawerPosition(-drawerWidthPx)  // Close drawer
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

