package com.example.todo.presentation.receiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.todo.R

class ReminderReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        val notification = NotificationCompat.Builder(context, "todo_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Todo Reminder")
            .setContentText("Don't forget to complete this task!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ){
            NotificationManagerCompat.from(context).notify(1, notification)
        }

    }
}