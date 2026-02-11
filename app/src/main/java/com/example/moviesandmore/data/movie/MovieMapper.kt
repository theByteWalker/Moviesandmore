package com.example.moviesandmore.data.movie

import com.example.moviesandmore.domain.movie.Movie

class MovieMapper {
    fun toDomain(dto: MovieDto): Movie {
        return Movie(
            name = dto.primaryTitle,
            imageUrl = dto.primaryImage?.url
        )
    }

    fun toDomainList(dtos: List<MovieDto>): List<Movie> {
        return dtos.map { toDomain(it) }
    }
}