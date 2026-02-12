package com.example.moviesandmore.app

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesandmore.data.FavoriteDao
import com.example.moviesandmore.data.FavoriteMovieEntity
import com.example.moviesandmore.data.MovieDao
import com.example.moviesandmore.data.MovieEntity

@Database(entities = [MovieEntity::class, FavoriteMovieEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun favouriteDao(): FavoriteDao
}