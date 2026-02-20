package com.example.moviesandmore.domain.movie

//import javax.inject.Inject

// Replaced by GetPopularMoviesPagerUseCase which returns Flow<PagingData<Movie>>
// for Paging 3 integration. This one-shot approach doesn't support pagination.

//class GetAllPopularMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) {
//    suspend fun execute(): List<Movie> {
//        return movieRepository.getAllPopularMovies()
//    }
//}