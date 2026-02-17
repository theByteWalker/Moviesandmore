import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.core.content.edit
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(url: String, modifier: Modifier = Modifier, onFullScreenToggle: (Boolean) -> Unit) {
    val context = LocalContext.current
    val activity = context as? Activity

    val sharedPrefs = remember { context.getSharedPreferences("video_prefs", Context.MODE_PRIVATE) }
    val videoKey = "timestamp_$url"

    var isBuffering by remember { mutableStateOf(true) }
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    isBuffering = playbackState == Player.STATE_BUFFERING
                }
            })
            val savedPosition = sharedPrefs.getLong(videoKey, 0L)
            seekTo(savedPosition)
            playWhenReady = true
            prepare()
        }
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer

                    setFullscreenButtonClickListener { isClickFullScreen ->
                        onFullScreenToggle(isClickFullScreen)
                        val window = activity?.window ?: return@setFullscreenButtonClickListener
                        val controller = WindowCompat.getInsetsController(window, window.decorView)

                        if (isClickFullScreen) {
                            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            controller.hide(WindowInsetsCompat.Type.systemBars())
                        } else {
                            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            controller.show(WindowInsetsCompat.Type.systemBars())
                        }
                    }
                }
            },
            modifier = modifier,
            update = { view ->
                view.useController = !isBuffering
            }
        )
        if (isBuffering) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            val currentPos = exoPlayer.currentPosition
            val totalDuration = exoPlayer.duration
            if (totalDuration > 0 && (totalDuration - currentPos) < 1000) {
                sharedPrefs.edit { putLong(videoKey, 0L) }
            } else {
                sharedPrefs.edit { putLong(videoKey, currentPos) }
            }
            exoPlayer.release()
        }
    }
}