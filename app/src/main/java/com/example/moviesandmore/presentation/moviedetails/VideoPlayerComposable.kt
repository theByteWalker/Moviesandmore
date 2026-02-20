package com.example.moviesandmore.presentation.moviedetails

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity

    var isFullscreen by remember { mutableStateOf(false) }
    var isBuffering by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(true) }
    val positionManager = remember { VideoPositionManager(context) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            
            val savedPosition = positionManager.getPosition(videoUrl)
            if (savedPosition > 0) {
                seekTo(savedPosition)
            }
            playWhenReady = true
            volume = 0f
            
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    isBuffering = playbackState == Player.STATE_BUFFERING
                }
            })
        }
    }

    SideEffect {
        activity?.let {
            if (isFullscreen) {
                enterFullscreenMode(it)
            } else {
                exitFullscreen(it)
            }
        }
    }

    DisposableEffect(videoUrl) {
        onDispose {
            val currentPosition = exoPlayer.currentPosition
            if (currentPosition > 0) {
                positionManager.savePosition(videoUrl, currentPosition)
            }
            
            exoPlayer.release()
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    BackHandler(enabled = isFullscreen) {
        isFullscreen = false
    }

    if (!isFullscreen) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
        ) {
            AndroidView(
                factory = {
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = true
                        setFullscreenButtonClickListener { enterFullscreen ->
                            isFullscreen = enterFullscreen
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            
            if (isBuffering) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }
            
            IconButton(
                onClick = {
                    isMuted = !isMuted
                    exoPlayer.volume = if (isMuted) 0f else 1f
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = if (isMuted) "Mute" else "Unmute",
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

    if (isFullscreen) {
        Dialog(
            onDismissRequest = {
                isFullscreen = false
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                AndroidView(
                    factory = {
                        PlayerView(context).apply {
                            player = exoPlayer
                            useController = true
                            setFullscreenButtonClickListener { enterFullscreen ->
                                isFullscreen = enterFullscreen
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
                
                if (isBuffering) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }
                
                IconButton(
                    onClick = {
                        isMuted = !isMuted
                        exoPlayer.volume = if (isMuted) 0f else 1f
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Black.copy(alpha = 0.5f)
                    )
                ) {
                    Text(
                        text = if (isMuted) "Mute" else "Unmute",
                        color = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

private fun enterFullscreenMode(activity: Activity) {
//    val window = activity.window
//    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
//
//    // Hide system bars
//    windowInsetsController.systemBarsBehavior =
//        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

    // Set landscape orientation
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
}

private fun exitFullscreen(activity: Activity) {
//    val window = activity.window
//    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
//
//    // Show system bars
//    windowInsetsController.show(WindowInsetsCompat.Type.systemBars())


    // Reset to portrait orientation
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}