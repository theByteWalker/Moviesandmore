package com.example.moviesandmore.app

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFirebaseMessagingService: FirebaseMessagingService() {
    @Inject
    lateinit var favouritesNotificationService: FavouritesNotificationService

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val movieTitle = if (remoteMessage.data.isNotEmpty()) {
            remoteMessage.data["movie_title"]
        } else {
            remoteMessage.notification?.body
        }

        movieTitle?.let { title ->
            favouritesNotificationService.showPromotionalNotification(title)
        }
    }
}