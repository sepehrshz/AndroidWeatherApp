package com.example.weatherapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun DailyWeather() {
    val dayList = listOf("Tomorrow" , "Saturday" , "Sunday" , "Monday" , "Tuesday")
//    val scrollState = rememberScrollState()

    Column(modifier = Modifier.padding(start=16.dp, bottom = 45.dp, end = 16.dp, top =40.dp))
//        .verticalScroll(scrollState))
    {

        dayList.forEach{
            Row(
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 6.dp)
                  .background(Color.White.copy(alpha = 0.4f) , shape = RoundedCornerShape(16.dp))
                  .padding(12.dp),

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(it ,color = Color.White.copy(alpha = 0.7f) , style = MaterialTheme.typography.bodySmall)
                Row(verticalAlignment = Alignment.CenterVertically){
                    Image(painter = painterResource(id= R.drawable.cloudy_weather) , contentDescription = "Cloudy")
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text("18°/19°", color = Color.White)
                }
            }
        }
    }
}