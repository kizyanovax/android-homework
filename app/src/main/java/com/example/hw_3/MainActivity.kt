package com.example.hw_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.hw_3.profile.notifications.NotificationHelper
import com.example.hw_3.core.ui.theme.Hw3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Создаём канал уведомлений при запуске приложения
        NotificationHelper.createNotificationChannel(this)
        setContent {
            Hw3Theme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    var buttonsVisible by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            if (buttonsVisible) {
                BottomBar(
                    navController = navController,
                    state = buttonsVisible,
                    modifier = Modifier
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavigationGraph(
                navController = navController,
                onBottomBarVisibilityChanged = { isVisible ->
                    buttonsVisible = isVisible
                }
            )
        }
    }
}