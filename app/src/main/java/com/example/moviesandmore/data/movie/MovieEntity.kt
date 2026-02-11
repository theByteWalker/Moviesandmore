package com.example.moviesandmore.data.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "poster_url") val posterUrl: String
)
