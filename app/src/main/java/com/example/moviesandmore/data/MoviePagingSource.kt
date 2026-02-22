package com.example.moviesandmore.data

import androidx.paging.PagingSource
import androidx.paging.PagingState

class MoviePagingSource(
    private val apiService: MovieApiService
) : PagingSource<String, MovieTitle>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, MovieTitle> {
        return try {
            val currentKey = params.key
            val response = apiService.getTitles(pageToken = currentKey)

            val nextKey = if (response.titles.isEmpty() || response.nextPageToken.isNullOrEmpty() || response.nextPageToken == currentKey) {
                null
            } else {
                response.nextPageToken
            }

            LoadResult.Page(
                data = response.titles,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, MovieTitle>): String? {
        return null
    }
}