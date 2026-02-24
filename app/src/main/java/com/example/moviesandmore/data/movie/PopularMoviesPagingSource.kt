package com.example.moviesandmore.data.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviesandmore.core.network.ApiResult
import com.example.moviesandmore.core.network.safeApiCall
import com.example.moviesandmore.core.utils.Logger
import com.example.moviesandmore.domain.movie.Movie
import kotlinx.coroutines.delay

private const val TAG = "PagingSource"

class PopularMoviesPagingSource(
    private val movieApiService: MovieApiService,
    private val movieMapper: MovieMapper,
    private val logger: Logger
) : PagingSource<String, Movie>() {

    override fun getRefreshKey(state: PagingState<String, Movie>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Movie> {
        return try {
            val pageToken = params.key
            logger.d(TAG, "Loading page with token: $pageToken")

            if (pageToken != null) {
                delay(5000)
            }

            val result = safeApiCall { movieApiService.getAllPopularMovies(pageToken = pageToken) }

            when (result) {
                is ApiResult.Success -> {
                    val body = result.data
                    val movies = movieMapper.toDomainList(body.titles ?: emptyList())
                    logger.d(TAG, "Loaded ${movies.size} movies | nextPageToken: ${body.nextPageToken}")

                    return LoadResult.Page(
                        data = movies,
                        prevKey = null,
                        nextKey = body.nextPageToken
                    )
                }
                is ApiResult.Error -> {
                    logger.e(TAG, "API failed: ${result.message}")
                    return LoadResult.Error(Exception("Failed to load movies: ${result.message}"))
                }
                is ApiResult.Exception -> {
                    return LoadResult.Error(result.throwable)
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
