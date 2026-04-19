package ru.rtmrslnv.androidpractice.views

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.core.app.NotificationCompat
import ru.rtmrslnv.androidpractice.MainActivity
import ru.rtmrslnv.androidpractice.R

class NotifyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val name = intent.getStringExtra("name") ?: "Пара"
        val channelId = "fav_class_channel"

        val openIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val openPending = PendingIntent.getActivity(
            context, 0, openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(channelId, "Любимая пара", NotificationManager.IMPORTANCE_HIGH)
            nm.createNotificationChannel(ch)
        }

        val n = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Сработало напоминание")
            .setContentText("Пора на пару, $name!")
            .setContentIntent(openPending)
            .setAutoCancel(true)
            .build()

        nm.notify(1001, n)
    }
}
