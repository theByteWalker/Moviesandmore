package com.example.moviesandmore.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: FavoriteMovieEntity)

    @Query("DELETE FROM favorites WHERE id = :titleId")
    suspend fun deleteFavoriteById(titleId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :titleId)")
    suspend fun isFavorite(titleId: String): Boolean

    @Query("SELECT * FROM favorites ORDER BY createdAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteMovieEntity>>
}