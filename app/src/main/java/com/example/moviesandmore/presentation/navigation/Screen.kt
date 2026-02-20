package com.example.moviesandmore.presentation.navigation

sealed class Screen(val route: String) {
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    object Album : Screen("album")
    data object MovieDetail : Screen("movieDetail/{movieId}"){
        fun createRoute(movieId: String) = "movieDetail/$movieId"
    }
}
