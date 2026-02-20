package com.example.moviesandmore.presentation.moviedetails

import android.content.ComponentName
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerView
import com.example.moviesandmore.app.AudioPlaybackService
import com.google.common.util.concurrent.MoreExecutors

@Composable
fun MusicPlayerComposable() {
    val context = LocalContext.current
    val audioUrl: String = "https://storage.googleapis.com/exoplayer-test-media-0/Jazz_In_Paris.mp3"

    var player: Player? by remember { mutableStateOf(null) }

    val controllerFuture = remember {
        val sessionToken = SessionToken(
            context,
            ComponentName(context, AudioPlaybackService::class.java)
        )
        MediaController.Builder(context, sessionToken).buildAsync()
    }

    DisposableEffect(Unit) {
        controllerFuture.addListener(
            {
                val controller = controllerFuture.get()
                controller.setMediaItem(MediaItem.fromUri(audioUrl))
                controller.prepare()
                player = controller
            },
            MoreExecutors.directExecutor()
        )
        onDispose {
            MediaController.releaseFuture(controllerFuture)
            player = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f)
    ) {
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    useController = true
                }
            },
            update = { playerView ->
                playerView.player = player
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}