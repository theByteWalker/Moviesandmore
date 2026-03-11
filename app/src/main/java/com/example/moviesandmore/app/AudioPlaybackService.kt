package com.example.moviesandmore.app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

class AudioPlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private var sleepTimerHandler: Handler? = null
    private var sleepTimerRunnable: Runnable? = null
    private var targetTimeMillis: Long = 0L

    companion object {
        const val COMMAND_START_SLEEP_TIMER = "START_SLEEP_TIMER"
        const val COMMAND_CANCEL_SLEEP_TIMER = "CANCEL_SLEEP_TIMER"
        const val EXTRA_DURATION_MS = "DURATION_MS"
    }

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (!isPlaying && TimerStateManager.timerState.value.isActive) {
                // If user manually pauses, cancel the timer
                cancelSleepTimer()
            }
        }
    }

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()
        player.addListener(playerListener)
        
        val callback = object : MediaSession.Callback {
            override fun onConnect(
                session: MediaSession,
                controller: MediaSession.ControllerInfo
            ): MediaSession.ConnectionResult {
                val connectionResult = super.onConnect(session, controller)
                val sessionCommands = connectionResult.availableSessionCommands
                    .buildUpon()
                    .add(SessionCommand(COMMAND_START_SLEEP_TIMER, Bundle.EMPTY))
                    .add(SessionCommand(COMMAND_CANCEL_SLEEP_TIMER, Bundle.EMPTY))
                    .build()
                return MediaSession.ConnectionResult.accept(sessionCommands, connectionResult.availablePlayerCommands)
            }

            override fun onCustomCommand(
                session: MediaSession,
                controller: MediaSession.ControllerInfo,
                customCommand: SessionCommand,
                args: Bundle
            ): ListenableFuture<SessionResult> {
                when (customCommand.customAction) {
                    COMMAND_START_SLEEP_TIMER -> {
                        val durationMs = args.getLong(EXTRA_DURATION_MS, 0L)
                        if (durationMs > 0) {
                            startSleepTimer(durationMs)
                        }
                    }
                    COMMAND_CANCEL_SLEEP_TIMER -> {
                        cancelSleepTimer()
                    }
                }
                return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
            }
        }

        mediaSession = MediaSession.Builder(this, player)
            .setCallback(callback)
            .build()
            
        sleepTimerHandler = Handler(Looper.getMainLooper())
    }

    private fun startSleepTimer(durationMs: Long) {
        cancelSleepTimerRunnable() // Cancel any existing runnable
        
        targetTimeMillis = System.currentTimeMillis() + durationMs
        
        sleepTimerRunnable = object : Runnable {
            override fun run() {
                val remaining = targetTimeMillis - System.currentTimeMillis()
                
                if (remaining <= 0) {
                    // Timer finished
                    mediaSession?.player?.pause()
                    mediaSession?.player?.volume = 1f // Reset volume for next time
                    TimerStateManager.clearState()
                    return
                }
                
                TimerStateManager.updateState(true, remaining)
                
                // Fade out in the last 5 seconds
                if (remaining <= 5000L) {
                    val volume = (remaining / 5000f).coerceIn(0f, 1f)
                    mediaSession?.player?.volume = volume
                }
                
                sleepTimerHandler?.postDelayed(this, 1000L)
            }
        }
        
        sleepTimerHandler?.post(sleepTimerRunnable!!)
    }

    private fun cancelSleepTimerRunnable() {
        sleepTimerRunnable?.let { sleepTimerHandler?.removeCallbacks(it) }
        sleepTimerRunnable = null
    }

    private fun cancelSleepTimer() {
        cancelSleepTimerRunnable()
        mediaSession?.player?.volume = 1f // Reset volume
        TimerStateManager.clearState()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        cancelSleepTimerRunnable()
        mediaSession?.run {
            player.removeListener(playerListener)
            player.release()
            release()
        }
        mediaSession = null
        super.onDestroy()
    }
}
