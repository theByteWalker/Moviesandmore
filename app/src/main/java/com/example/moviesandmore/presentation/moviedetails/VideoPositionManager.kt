package com.example.moviesandmore.presentation.moviedetails

import android.content.Context
import android.content.SharedPreferences
class VideoPositionManager(context: Context) {
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    fun savePosition(videoUrl: String, position: Long) {
        sharedPreferences.edit()
            .putLong(videoUrl.toString(), position)
            .apply()
    }
    fun getPosition(videoUrl: String): Long {
        return sharedPreferences.getLong(videoUrl.toString(), 0L)
    }
    companion object {
        private const val PREFS_NAME = "video_playback_positions"
    }
}