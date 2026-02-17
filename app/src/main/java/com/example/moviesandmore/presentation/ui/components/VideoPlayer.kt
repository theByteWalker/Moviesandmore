import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(url: String, modifier: Modifier = Modifier, onFullScreenToggle: (Boolean) -> Unit) {
    val context = LocalContext.current
    val activity = context as? Activity

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            playWhenReady = true
        }
    }

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
        modifier = modifier
    )

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}