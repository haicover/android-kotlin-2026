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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haicover.smoketest.ui.theme.SmokeTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmokeTestTheme {
                CounterRoute()
            }
        }
    }
}

/**
 * Composable Stateful đóng vai trò định tuyến và quản lý trạng thái của Counter.
 * Sử dụng rememberSaveable để bảo toàn state qua configuration changes và process death.
 */
@Composable
fun CounterRoute() {
    var count by rememberSaveable {
        mutableIntStateOf(0)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        CounterContent(
            count = count,
            onIncrement = { count++ }
        )
    }
}

/**
 * Composable Stateless hiển thị giao diện của Counter.
 * Nhận count dạng dữ liệu bất biến và truyền phát sự kiện qua onIncrement.
 */
@Composable
fun CounterContent(
    count: Int,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
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
            onClick = onIncrement
        ) {
            Text("Bắt đầu hành trình")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CounterContentZeroPreview() {
    SmokeTestTheme {
        CounterContent(
            count = 0,
            onIncrement = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CounterContentFivePreview() {
    SmokeTestTheme {
        CounterContent(
            count = 5,
            onIncrement = {}
        )
    }
}
