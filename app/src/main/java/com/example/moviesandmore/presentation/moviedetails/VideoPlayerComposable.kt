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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.MediaItem
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

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
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

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    BackHandler(enabled = isFullscreen) {
        isFullscreen = false
    }

    if (!isFullscreen) {
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
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
        )
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