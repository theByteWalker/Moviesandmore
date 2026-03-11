package com.example.moviesandmore.presentation.moviedetails

import android.content.ComponentName
import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerView
import com.example.moviesandmore.app.AudioPlaybackService
import com.example.moviesandmore.app.TimerStateManager
import com.google.common.util.concurrent.MoreExecutors

@Composable
fun MusicPlayerComposable() {
    val context = LocalContext.current
    val audioUrl: String = "https://storage.googleapis.com/exoplayer-test-media-0/Jazz_In_Paris.mp3"

    var player: Player? by remember { mutableStateOf(null) }
    val timerState by TimerStateManager.timerState.collectAsState()

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

    Column(modifier = Modifier.fillMaxWidth()) {
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
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sleep Timer",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            if (timerState.isActive) {
                val totalSeconds = timerState.timeRemainingMillis / 1000
                val minutes = totalSeconds / 60
                val seconds = totalSeconds % 60
                val timeString = String.format("%02d:%02d", minutes, seconds)
                
                Text(
                    text = "Sleep in: $timeString",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Button(
                    onClick = {
                        (player as? MediaController)?.let { controller ->
                            val command = SessionCommand(AudioPlaybackService.COMMAND_CANCEL_SLEEP_TIMER, Bundle.EMPTY)
                            controller.sendCustomCommand(command, Bundle.EMPTY)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cancel Timer")
                }
            }
            else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SleepTimerButton(player as? MediaController, "15s", 15 * 1000L)
                }
            }
        }
    }
}

@Composable
fun SleepTimerButton(controller: MediaController?, label: String, durationMs: Long) {
    Button(
        onClick = {
            controller?.let {
                val args = Bundle().apply {
                    putLong(AudioPlaybackService.EXTRA_DURATION_MS, durationMs)
                }
                val command = SessionCommand(AudioPlaybackService.COMMAND_START_SLEEP_TIMER, Bundle.EMPTY)
                it.sendCustomCommand(command, args)
            }
        },
        enabled = controller != null
    ) {
        Text(label)
    }
}