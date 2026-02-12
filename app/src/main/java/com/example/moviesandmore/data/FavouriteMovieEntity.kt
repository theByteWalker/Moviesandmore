package com.example.moviesandmore.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteMovieEntity(
    @PrimaryKey val id: String,
    val title: String,
    val imageUrl: String?,
    val rating: Int?,
    val year: Int?,
    val createdAt: Long = System.currentTimeMillis()
)