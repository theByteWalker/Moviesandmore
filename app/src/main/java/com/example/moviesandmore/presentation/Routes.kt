package com.example.moviesandmore.presentation

object Routes {
    const val POPULAR = "popular"
    const val FAVOURITES = "favourites"
    const val SEARCH = "search"
    const val TITLE_ID = "titleId"
    const val MOVIE_DETAILS = "details/{$TITLE_ID}"
    fun navigateToMovieDetailsById(titleId: String): String {
        return "details/$titleId"
    }
    const val LOGIN = "login"
    const val ALBUM = "album"
    const val RICK_AND_MORTY = "rick_and_morty"
}