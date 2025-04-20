package com.example.weatherapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Header() {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Button(
                        onClick = { /* Sign up button clicked */ },
                        modifier = Modifier.padding(8.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8CC8FF),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Sign up")
                    }

                    Button(
                        onClick = { /* Login button clicked */ },
                        modifier = Modifier.padding(8.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8CC8FF),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Login")
                    }
                }
            }
        }
    }