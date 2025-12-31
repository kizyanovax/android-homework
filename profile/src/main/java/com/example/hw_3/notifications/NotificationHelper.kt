package com.example.hw_3.profile.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

object NotificationHelper {
    private const val CHANNEL_ID = "favorite_class_channel"
    private const val CHANNEL_NAME = "Напоминания о паре"
    private const val NOTIFICATION_ID = 1

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            channel.description = "Уведомления о начале любимой пары"
            channel.enableVibration(true)
            channel.enableLights(true)
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showFavoriteClassNotification(context: Context, userName: String, mainActivityClass: Class<out android.app.Activity>) {
        Log.d("NotificationHelper", "showFavoriteClassNotification called for user: $userName")
        
        // Создаём канал, если его ещё нет
        createNotificationChannel(context)

        // Intent для открытия приложения при нажатии на уведомление
        val intent = Intent(context, mainActivityClass).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = androidx.core.app.NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Начало любимой пары!")
            .setContentText("$userName, пора на пару по мобильной разработке!")
            .setStyle(androidx.core.app.NotificationCompat.BigTextStyle()
                .bigText("$userName, пора на пару по мобильной разработке!"))
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
            .setDefaults(androidx.core.app.NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        // Проверяем, что уведомления разрешены
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!notificationManager.areNotificationsEnabled()) {
                Log.w("NotificationHelper", "Notifications are not enabled")
                return
            }
        }
        
        notificationManager.notify(NOTIFICATION_ID, notification)
        Log.d("NotificationHelper", "Notification sent successfully")
    }
}

