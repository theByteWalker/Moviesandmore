package com.example.moviesandmore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviesandmore.presentation.favorites.FavoritesScreen
import com.example.moviesandmore.presentation.movie.MovieSearchScreen
import com.example.moviesandmore.presentation.moviedetails.MovieDetails
import com.example.moviesandmore.presentation.navigation.Screen
import com.example.moviesandmore.ui.theme.MoviesAndMoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesAndMoreTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        SimpleBottomBar(
                            onFavoritesClick = {
                                navController.navigate(Screen.Favorites.route) {
                                    launchSingleTop = true
                                }
                            },
                            onSearchClick = {
                                navController.navigate(Screen.Search.route) {
                                    launchSingleTop = true
                                    popUpTo(Screen.Search.route) { inclusive = true }
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Search.route,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = innerPadding.calculateBottomPadding())
                    ) {
                        composable(Screen.Search.route) {
                            MovieSearchScreen(
                                onCardClick = { movie ->
                                    navController.navigate(Screen.MovieDetail.createRoute(movie.titleId))
                                }
                            )
                        }
                        composable(Screen.Favorites.route) {
                            FavoritesScreen(
                                onCardClick = { movieId ->
                                    navController.navigate(Screen.MovieDetail.createRoute(movieId))
                                }
                            )
                        }
                        composable(Screen.MovieDetail.route, content = { backStackEntry ->
                            val movieId = backStackEntry.arguments?.getString("movieId")
                            MovieDetails(movieId)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleBottomBar(
    onFavoritesClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }

            IconButton(onClick = onFavoritesClick) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Favorites"
                )
            }
        }
    }
}