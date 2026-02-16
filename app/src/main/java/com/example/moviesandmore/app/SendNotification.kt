package com.example.moviesandmore.app

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.moviesandmore.R
import javax.inject.Inject

class SendNotification @Inject constructor() {
    fun triggerNotification(context: Context, title: String, text: String) {
        val builder = NotificationCompat.Builder(context, "LOGIN_CHANNEL")
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(1, builder.build())
        } else {
            Log.e("NotificationError", "User has not granted POST_NOTIFICATIONS permission")
        }
    }
}