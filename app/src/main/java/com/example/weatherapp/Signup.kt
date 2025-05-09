// CreateAccountScreen.kt
package com.example.weatherapp

import android.content.Intent
import android.net.Uri
import android.util.Patterns
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.weatherapp.network.ApiClient
import com.example.weatherapp.network.model.signup.SignupRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Returns list of unmet password requirements.
 */
fun validatePassword(password: String): List<String> {
    val missing = mutableListOf<String>()
    if (password.length < 8) missing.add("at least 8 characters")
    if (!password.any { it.isUpperCase() }) missing.add("one uppercase letter")
    if (!password.any { it.isLowerCase() }) missing.add("one lowercase letter")
    if (!password.any { it.isDigit() }) missing.add("one digit")
    if (!password.any { "!@#\$%^&*()-_=+[]{}|;:'\",.<>?/`~".contains(it) })
        missing.add("one special character")
    return missing
}

@Composable
fun CreateAccountScreen(
    primaryColor: Color = Color(0xFF008BF8),
    titleColor: Color = primaryColor,
    subtitleColor: Color = Color(0xFF000000),
    inputBackgroundColor: Color = Color(0xFFECF0FC),
    iconBackgroundColor: Color = Color(0xFFE7E7E7),
    iconSpacing: Dp = 16.dp,
    LoginScreen: () -> Unit = {},
    onSignUpSuccess: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    BackHandler { onBack() }
    val focusManager = LocalFocusManager.current
    val config = LocalConfiguration.current
    val topPadding = config.screenHeightDp.dp * 0.1f
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var email                   by remember { mutableStateOf("") }
    var password                by remember { mutableStateOf("") }
    var confirmPassword         by remember { mutableStateOf("") }

    var emailError              by remember { mutableStateOf(false) }
    var passwordError           by remember { mutableStateOf(false) }
    var confirmPasswordError    by remember { mutableStateOf(false) }

    var emailErrorMessage       by remember { mutableStateOf("") }
    var passwordErrorMessage    by remember { mutableStateOf("") }
    var confirmPasswordErrorMessage by remember { mutableStateOf("") }

    var loading                 by remember { mutableStateOf(false) }
    var apiError                by remember { mutableStateOf<String?>(null) }
    var valid                   by remember { mutableStateOf(true) }

    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = topPadding, start = 24.dp, end = 24.dp, bottom = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text("Create Account", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = titleColor)
            Text(
                "Create an account so you can explore all the existing jobs",
                fontSize = 18.sp, fontWeight = FontWeight.Bold, color = subtitleColor,
                textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 12.dp)
            )
            Spacer(Modifier.height(70.dp))

            // Email
            InputField(
                value = email,
                label = "Email",
                backgroundColor = inputBackgroundColor,
                isError = emailError,
                errorMessage = emailErrorMessage
            ) {
                email = it
                if (emailError) {
                    emailError = false
                    emailErrorMessage = ""
                }
            }
            Spacer(Modifier.height(20.dp))

            // Password
            InputField(
                value = password,
                label = "Password",
                isPassword = true,
                backgroundColor = inputBackgroundColor,
                isError = passwordError,
                errorMessage = passwordErrorMessage
            ) {
                password = it
                if (passwordError) {
                    passwordError = false
                    passwordErrorMessage = ""
                }
            }

            // Strength Meter + Label
            if (password.isNotEmpty()) {
                val missing = validatePassword(password)
                val totalRules = 5f
                val metRules = totalRules - missing.size
                val strengthFraction = (metRules / totalRules).coerceIn(0f, 1f)

                // Determine label & target color
                val (strengthLabel, targetColor) = when {
                    missing.isEmpty()         -> "Strong" to Color.Green
                    strengthFraction >= 0.6f  -> "Medium" to Color(0xFFFFC107)
                    else                      -> "Weak" to Color.Red
                }

                val animatedColor by animateColorAsState(targetColor)

                LinearProgressIndicator(
                    progress   = strengthFraction,
                    color      = animatedColor,
                    trackColor = Color.LightGray,
                    modifier   = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .padding(vertical = 8.dp)
                )
                Text(
                    text = strengthLabel,
                    color = animatedColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
            } else {
                Spacer(Modifier.height(32.dp))
            }

            // Confirm Password
            InputField(
                value = confirmPassword,
                label = "Confirm Password",
                isPassword = true,
                backgroundColor = inputBackgroundColor,
                isError = confirmPasswordError,
                errorMessage = confirmPasswordErrorMessage
            ) {
                confirmPassword = it
                if (confirmPasswordError) {
                    confirmPasswordError = false
                    confirmPasswordErrorMessage = ""
                }
            }
            Spacer(Modifier.height(48.dp))

            // Sign up Button
            Button(
                onClick = {
                    focusManager.clearFocus()
                    // reset flags
                    emailError = false; passwordError = false; confirmPasswordError = false
                    emailErrorMessage = ""; passwordErrorMessage = ""; confirmPasswordErrorMessage = ""
                    valid = true

                    // Email
                    if (email.isBlank()) {
                        emailError = true
                        emailErrorMessage = "Please enter email"
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailError = true
                        emailErrorMessage = "Invalid email address"
                    }

                    // Password
                    if (password.isBlank()) {
                        passwordError = true
                        passwordErrorMessage = "Please enter password"
                    } else {
                        val m = validatePassword(password)
                        if (m.isNotEmpty()) {
                            passwordError = true
                            passwordErrorMessage = "Password is too weak: missing ${m.joinToString(", ")}"
                        }
                    }

                    // Confirm Password
                    if (confirmPassword.isBlank()) {
                        confirmPasswordError = true
                        confirmPasswordErrorMessage = "Please confirm password"
                    } else if (confirmPassword != password) {
                        confirmPasswordError = true
                        confirmPasswordErrorMessage = "Passwords do not match"
                    }

                    // Call API
                    if (!emailError && !passwordError && !confirmPasswordError) {
                        loading = true; apiError = null
                        scope.launch {
                            try {
                                val resp = withContext(Dispatchers.IO) {
                                    ApiClient.apiService.signup(SignupRequest(email, password)).execute()
                                }
                                if (resp.isSuccessful && resp.body()?.objectId != null) {
                                    onSignUpSuccess()
                                } else {
                                    valid = false
                                    apiError = resp.body()?.message ?: "Signup failed"
                                }
                            } catch (e: Exception) {
                                valid = false
                                apiError = "Network error: ${e.localizedMessage}"
                            } finally {
                                loading = false
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = buttonColors(containerColor = primaryColor)
            ) {
                Text("Sign up", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            if (!valid && apiError != null) {
                Text(apiError!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(Modifier.height(40.dp))
            Text(
                "Already have an account",
                fontSize = 14.sp, fontWeight = FontWeight.Bold,
                color = Color(0xFF616160),
                modifier = Modifier.clickable { LoginScreen() }
            )
            Spacer(Modifier.height(40.dp))
            Text(
                "Or continue with",
                fontSize = 14.sp, fontWeight = FontWeight.Bold, color = primaryColor
            )
            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(iconSpacing, Alignment.CenterHorizontally)
            ) {
                SocialButton(R.drawable.google, iconBackgroundColor, "https://www.google.com")
                SocialButton(R.drawable.facebook, iconBackgroundColor, "https://www.facebook.com")
                SocialButton(R.drawable.apple, iconBackgroundColor, "https://www.apple.com")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
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
        colors = outlinedTextFieldColors(
            containerColor       = backgroundColor,
            focusedBorderColor   = if (isError) MaterialTheme.colorScheme.error else Color(0xFF008BF8),
            unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else backgroundColor.copy(alpha = 0.5f),
            cursorColor          = if (isError) MaterialTheme.colorScheme.error else Color.Black,
            focusedLabelColor    = if (isError) MaterialTheme.colorScheme.error else Color.Black
        ),
        supportingText = {
            if (isError) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
        }
    )
}

@Composable
fun SocialButton(
    @DrawableRes imageRes: Int,
    backgroundColor: Color,
    url: String
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(if (isHovered) 1.2f else 1f)

    Box(
        Modifier
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
fun CreateAccountScreenPreview() {
    MaterialTheme {
        CreateAccountScreen(onSignUpSuccess = {}, onBack = {})
    }
}
