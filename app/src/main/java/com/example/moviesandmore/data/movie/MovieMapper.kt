package com.example.moviesandmore.data.movie

import com.example.moviesandmore.domain.movie.Movie

class MovieMapper {
    fun toDomain(dto: MovieDto): Movie {
        return Movie(
            titleId = dto.id,
            name = dto.primaryTitle,
            imageUrl = dto.primaryImage?.url
        )
    }

    fun toDomainList(dtos: List<MovieDto>): List<Movie> {
        return dtos.map { toDomain(it) }
    }

    fun toEntity(movie: Movie): MovieEntity {
        return MovieEntity(
            titleId = movie.titleId,
            name = movie.name,
            posterUrl = movie.imageUrl ?: ""
        )
    }

    fun toDomainFromDetail(dto: MovieDetailDto): Movie {
        return Movie(
            titleId = dto.id,
            name = dto.primaryTitle,
            imageUrl = dto.primaryImage?.url
        )
    }
}