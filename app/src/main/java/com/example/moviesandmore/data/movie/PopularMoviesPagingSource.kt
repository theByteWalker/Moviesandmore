package com.example.moviesandmore.data.movie

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviesandmore.domain.movie.Movie
import kotlinx.coroutines.delay

private const val TAG = "PagingSource"

class PopularMoviesPagingSource(
    private val movieApiService: MovieApiService,
    private val movieMapper: MovieMapper
) : PagingSource<String, Movie>() {

    override fun getRefreshKey(state: PagingState<String, Movie>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Movie> {
        return try {
            val pageToken = params.key
            Log.d(TAG, "Loading page with token: $pageToken")

            if (pageToken != null) {
                delay(5000)
            }

            val response = movieApiService.getAllPopularMovies(pageToken = pageToken)
            val body = response.body()

            if (!response.isSuccessful || body == null) {
                Log.e(TAG, "API failed: isSuccessful=${response.isSuccessful}, body=$body")
                return LoadResult.Error(Exception("Failed to load movies"))
            }

            val movies = movieMapper.toDomainList(body.titles ?: emptyList())
            Log.d(TAG, "Loaded ${movies.size} movies | nextPageToken: ${body.nextPageToken}")

            LoadResult.Page(
                data = movies,
                prevKey = null,
                nextKey = body.nextPageToken
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
