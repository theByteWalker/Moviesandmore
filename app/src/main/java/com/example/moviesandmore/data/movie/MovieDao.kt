package com.example.moviesandmore.data.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert
    fun save(movie: MovieEntity)

    @Query("SELECT * FROM movies")
    fun getAll(): List<MovieEntity>
}