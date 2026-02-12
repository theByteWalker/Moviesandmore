package com.example.moviesandmore.presentation.navigation

sealed class Screen(val route: String) {
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    data object MovieDetail : Screen("movieDetail/{movieId}"){
        fun createRoute(movieId: String) = "movieDetail/$movieId"
    }
}
