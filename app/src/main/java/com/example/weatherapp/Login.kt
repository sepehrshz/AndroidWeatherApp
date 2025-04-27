package com.example.weatherapp

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(

    primaryColor: Color = Color(0xFF008BF8),
    titleColor: Color = primaryColor,
    subtitleColor: Color = Color.Black,
    inputBackgroundColor: Color = Color(0xFFECF0FC),
    iconBackgroundColor: Color = Color(0xFFE7E7E7),
    iconSpacing: Dp = 16.dp,
    CreateAccountScreen: () -> Unit = {},
    onSignInSuccess: () -> Unit = {}       // ← اضافه شد

) {
    val focusManager = LocalFocusManager.current
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    val topPadding = screenHeightDp * 0.1f

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topPadding, start = 24.dp, end = 24.dp, bottom = 16.dp)
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Login here",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = titleColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Subtitle
        Text(
            text = "Welcome back you’ve been missed!",
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            color = subtitleColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 12.dp)
        )
        Spacer(modifier = Modifier.height(70.dp))

        // Email field
        LoginTextField(
            value = email,
            label = "Email",
            backgroundColor = inputBackgroundColor,
            isError = emailError,
            errorMessage = "Please enter email"
        ) {
            email = it
            if (emailError && it.isNotBlank()) emailError = false
        }
        Spacer(modifier = Modifier.height(35.dp))

        // Password field
        LoginTextField(
            value = password,
            label = "Password",
            isPassword = true,
            backgroundColor = inputBackgroundColor,
            isError = passwordError,
            errorMessage = "Please enter password"
        ) {
            password = it
            if (passwordError && it.isNotBlank()) passwordError = false
        }
        // ← این بخش اضافه شد:
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Forgot your password?",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* TODO: navigate to ForgotPasswordScreen */ }
        )
        // ← تا اینجا

        Spacer(modifier = Modifier.height(35.dp))

        // Sign in button
        Button(
            onClick = {
                focusManager.clearFocus()
                emailError = email.isBlank()
                passwordError = password.isBlank()
                if (!emailError && !passwordError) {
                    onSignInSuccess()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = buttonColors(containerColor = primaryColor)
        ) {
            Text(
                text = "Sign in",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(50.dp))

        // Create new account link
        Text(
            text = "Create new account",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF616160),
            modifier = Modifier.clickable { /* TODO */ }
                .clickable { CreateAccountScreen() }
        )
        Spacer(modifier = Modifier.height(70.dp))

        // Or continue with
        Text(
            text = "Or continue with",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Social login buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(iconSpacing, Alignment.CenterHorizontally)
        ) {
            LoginSocialButton(R.drawable.google, iconBackgroundColor, "https://www.google.com")
            LoginSocialButton(R.drawable.facebook, iconBackgroundColor, "https://www.facebook.com")
            LoginSocialButton(R.drawable.apple, iconBackgroundColor, "https://www.apple.com")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTextField(
    value: String,
    label: String,
    isPassword: Boolean = false,
    backgroundColor: Color,
    isError: Boolean = false,
    errorMessage: String = "",
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = isError,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor       = backgroundColor,
            focusedBorderColor   = if (isError) MaterialTheme.colorScheme.error else Color(0xFF008BF8),
            unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else backgroundColor.copy(alpha = 0.5f),
            cursorColor          = if (isError) MaterialTheme.colorScheme.error else Color.Black,
            focusedLabelColor    = if (isError) MaterialTheme.colorScheme.error else Color.Black
        ),
        supportingText = {
            if (isError) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    )
}

@Composable
fun LoginSocialButton(
    @DrawableRes imageRes: Int,
    backgroundColor: Color,
    url: String
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(if (isHovered) 1.2f else 1f)

    Box(
        modifier = Modifier
            .size(52.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .hoverable(interactionSource)
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(
            /* بقیه‌ی پارامترهای رنگ و … نیازی نیست چون پیش‌فرض دارن */
            CreateAccountScreen = {},
            onSignInSuccess     = { /* navigate to home */ }
        )
    }
}