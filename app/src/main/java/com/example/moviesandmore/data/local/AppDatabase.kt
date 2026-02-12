package com.example.moviesandmore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesandmore.data.movie.MovieDao
import com.example.moviesandmore.data.movie.MovieEntity

@Database(entities = [MovieEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase(){
    abstract fun movieDao(): MovieDao
}