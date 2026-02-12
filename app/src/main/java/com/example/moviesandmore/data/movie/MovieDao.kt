package com.example.moviesandmore.data.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert
    suspend fun save(movie: MovieEntity): Long

    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getById(id: Long): MovieEntity?
    
    @Query("SELECT * FROM movies")
    fun getAll(): Flow<List<MovieEntity>>
}