package com.haicover.smoketest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmokeTestApp()
        }
    }
}

@Composable
fun SmokeTestApp() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SmokeTestScreen()
        }
    }
}

@Composable
fun SmokeTestScreen() {
    var count by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello Hải — Android Kotlin 2026!",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Số lần bắt đầu: $count",
            fontSize = 18.sp
        )
        Button(
            modifier = Modifier.padding(top = 24.dp),
            onClick = { count += 1 }
        ) {
            Text("Bắt đầu hành trình")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SmokeTestScreenPreview() {
    SmokeTestApp()
}
