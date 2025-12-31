package com.example.hw_3.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "onReceive called with action: ${intent.action}")
        if (intent.action == "com.example.hw_3.ACTION_ALARM") {
            val userName = intent.getStringExtra("user_name") ?: "Пользователь"
            Log.d("AlarmReceiver", "Showing notification for user: $userName")
            NotificationHelper.showFavoriteClassNotification(context, userName)
        }
    }
}

