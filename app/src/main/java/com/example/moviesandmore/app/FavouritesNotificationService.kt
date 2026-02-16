package com.example.moviesandmore.app

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.moviesandmore.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouritesNotificationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showMovieAddedNotification(movieTitle: String) {
        val notification = NotificationCompat.Builder(
            context,
            Favourite_Movie_Added_Local_Notification_ID
        )
            .setSmallIcon(R.drawable.outline_bookmark_star_24)
            .setContentTitle("Movie Added to Favourites")
            .setContentText("$movieTitle has been added to your favourites")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }

    fun showPromotionalNotification(movieTitle: String) {
        val notification = NotificationCompat.Builder(
            context,
            Favourite_Movie_Added_Local_Notification_ID
        )
            .setSmallIcon(R.drawable.outline_bookmark_star_24)
            .setContentTitle("New Movie Live!")
            .setContentText("$movieTitle is live now. Add it to your favourites!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }

    companion object {
        const val Favourite_Movie_Added_Local_Notification_ID = "favourite_movie_added_local_notification"
    }
}