package com.example.moviesandmore.presentation.popularmovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviesandmore.domain.movie.Movie
import com.example.moviesandmore.domain.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    movieRepository: MovieRepository
) : ViewModel() {

    val moviesPagingFlow: Flow<PagingData<Movie>> =
        movieRepository.getPopularMoviesPager().cachedIn(viewModelScope)
}
