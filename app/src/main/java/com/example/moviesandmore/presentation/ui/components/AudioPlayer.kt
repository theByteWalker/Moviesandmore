package com.example.moviesandmore.presentation.ui.components

import android.content.ComponentName
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerView
import com.example.moviesandmore.app.PlaybackService
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    var showSleepOptions by remember { mutableStateOf(false) }
    var selectedDurationMinutes by remember { mutableStateOf<Int?>(null) }

    var waitingForPlay by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    var sleepJob by remember { mutableStateOf<Job?>(null) }

    fun cancelSleepJob() {
        sleepJob?.cancel()
    }

    fun startSleepTimerForMinutes(minutes: Int) {
        cancelSleepJob()
        val durationMs = minutes * 60 * 1000L

        val isPlaying = player?.isPlaying ?: false

        if (isPlaying) {
            sleepJob = coroutineScope.launch {
                delay(durationMs)
                player?.pause()
                selectedDurationMinutes = null
            }
        } else {
            waitingForPlay = true
            val listener = object : Player.Listener {
                override fun onIsPlayingChanged(isPlayingFlag: Boolean) {
                    if (isPlayingFlag && waitingForPlay) {
                        waitingForPlay = false
                        sleepJob = coroutineScope.launch {
                            delay(durationMs)
                            player?.pause()
                            selectedDurationMinutes = null
                        }
                        player?.removeListener(this)
                    }
                }
            }

            player?.addListener(listener)
        }
    }

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
                    controllerHideOnTouch = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            update = { playerView ->
                playerView.player = player
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { showSleepOptions = !showSleepOptions }) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = "Sleep Timer",
                modifier = Modifier.size(24.dp)
            )
        }

        if (selectedDurationMinutes != null) {
            Text(text = "Sleep timer: $selectedDurationMinutes min", modifier = Modifier.padding(start = 8.dp))
        } else {
            Text(text = "Set Sleep Timer", modifier = Modifier.padding(start = 8.dp))
        }
    }

    if (showSleepOptions) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            TextButton(onClick = {
                selectedDurationMinutes = 1
                showSleepOptions = false
                startSleepTimerForMinutes(1)
            }) {
                Text(text = "1 min")
            }

            TextButton(onClick = {
                selectedDurationMinutes = 3
                showSleepOptions = false
                startSleepTimerForMinutes(3)
            }) {
                Text(text = "3 min")
            }

            TextButton(onClick = {
                selectedDurationMinutes = 5
                showSleepOptions = false
                startSleepTimerForMinutes(5)
            }) {
                Text(text = "5 min")
            }

            if (selectedDurationMinutes != null) {
                TextButton(onClick = {
                    selectedDurationMinutes = null
                    waitingForPlay = false
                    cancelSleepJob()
                }) {
                    Text(text = "Cancel")
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            cancelSleepJob()
            MediaController.releaseFuture(controllerFuture)
        }
    }
}
