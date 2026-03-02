package com.example.moviesandmore.presentation.ui.components

import android.content.ComponentName
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerView
import com.example.moviesandmore.app.PlaybackService
import com.google.common.util.concurrent.MoreExecutors

@OptIn(UnstableApi::class)
@Composable
fun AudioPlayer(url: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sessionToken = remember {
        SessionToken(context, ComponentName(context, PlaybackService::class.java))
    }
    val controllerFuture = remember {
        MediaController.Builder(context, sessionToken).buildAsync()
    }
    var player by remember { mutableStateOf<MediaController?>(null) }

    LaunchedEffect(controllerFuture) {
        controllerFuture.addListener({
            player = controllerFuture.get()
            player?.let {
                if (it.mediaItemCount == 0) {
                    val mediaItem = MediaItem.Builder()
                        .setUri(url)
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setTitle("Soundtrack")
                                .setArtist("Movie Sample")
                                .build()
                        )
                        .build()
                    it.setMediaItem(mediaItem)
                    it.prepare()
                }
            }
        }, MoreExecutors.directExecutor())
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = true
                    // Show only the controller for audio
                    controllerHideOnTouch = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            update = { playerView ->
                playerView.player = player
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            MediaController.releaseFuture(controllerFuture)
        }
    }
}
