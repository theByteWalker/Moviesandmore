package com.example.moviesandmore.presentation.popularmovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviesandmore.domain.movie.GetPopularMoviesPagerUseCase
import com.example.moviesandmore.domain.movie.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    getPopularMoviesPagerUseCase: GetPopularMoviesPagerUseCase
) : ViewModel() {

    val moviesPagingFlow: Flow<PagingData<Movie>> =
        getPopularMoviesPagerUseCase().cachedIn(viewModelScope)
}
