package com.example.moviesandmore.ui.theme

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.moviesandmore.app.FavouritesNotificationService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoviesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                FavouritesNotificationService.Favourite_Movie_Added_Local_Notification_ID,
                "Favourite-Movie-Added-Local-Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Used to show notifications when a movie is added to favourites."

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}