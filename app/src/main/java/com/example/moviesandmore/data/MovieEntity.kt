package com.example.moviesandmore.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: String,
    val primaryTitle: String,
    val originalTitle: String?,
    val plot: String?,
    val startYear: Int?,
    val rating: Int?,
    val primaryImageUrl: String
)