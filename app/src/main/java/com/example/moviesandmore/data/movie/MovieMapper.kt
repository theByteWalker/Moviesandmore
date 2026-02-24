package com.example.moviesandmore.data.movie

import com.example.moviesandmore.core.data.Mapper
import com.example.moviesandmore.domain.movie.Movie

class MovieMapper : Mapper<MovieDto, Movie> {
    override fun map(input: MovieDto): Movie {
        return Movie(
            titleId = input.id,
            name = input.primaryTitle,
            imageUrl = input.primaryImage?.url
        )
    }

    fun toDomainList(dtos: List<MovieDto>): List<Movie> {
        return dtos.map { map(it) }
    }

    fun toEntity(movie: Movie): MovieEntity {
        return MovieEntity(
            titleId = movie.titleId,
            name = movie.name,
            posterUrl = movie.imageUrl ?: "",
            isFavorite = true
        )
    }

    fun toDomainFromEntity(entity: MovieEntity): Movie {
        return Movie(
            titleId = entity.titleId,
            name = entity.name,
            imageUrl = entity.posterUrl?.ifBlank { null }
        )
    }
}