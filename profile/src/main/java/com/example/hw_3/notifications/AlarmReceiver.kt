package com.example.hw_3.profile.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "onReceive called with action: ${intent.action}")
        if (intent.action == "com.example.hw_3.ACTION_ALARM") {
            val userName = intent.getStringExtra("user_name") ?: "Пользователь"
            val mainActivityClass = intent.getStringExtra("main_activity_class")?.let {
                try {
                    Class.forName(it) as Class<out android.app.Activity>
                } catch (e: Exception) {
                    Log.e("AlarmReceiver", "Failed to load MainActivity class", e)
                    null
                }
            }
            
            if (mainActivityClass != null) {
                Log.d("AlarmReceiver", "Showing notification for user: $userName")
                NotificationHelper.showFavoriteClassNotification(context, userName, mainActivityClass)
            } else {
                Log.e("AlarmReceiver", "MainActivity class not found in intent")
            }
        }
    }
}

