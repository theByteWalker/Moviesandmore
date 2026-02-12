package com.example.moviesandmore.app

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesandmore.data.MovieDao
import com.example.moviesandmore.data.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}