package com.example.moviesandmore.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moviesandmore.ui.theme.MoviesAndMoreTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.composable
import com.example.moviesandmore.presentation.ui.components.FavouritesMovies
import com.example.moviesandmore.presentation.ui.components.Login
import com.example.moviesandmore.presentation.ui.components.MovieDetails
import com.example.moviesandmore.presentation.ui.components.PopularMovies
import com.example.moviesandmore.presentation.ui.components.SearchMovies

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        val sharedPref = getSharedPreferences("movie_prefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", null)
        val startDest = if (username == "dummy_username") Routes.POPULAR else Routes.LOGIN
        enableEdgeToEdge()
        val channel = NotificationChannel(
            "LOGIN_CHANNEL",
            "Login Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        setContent {
            MoviesAndMoreTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val startDestination = Destination.POPULAR
                var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }
                val isLoginScreen = currentRoute == Routes.LOGIN
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (!isLoginScreen) {
                            CenterAlignedTopAppBar(
                                title = {
                                    val title = Destination.entries.find { it.route == currentRoute }?.title ?: ""
                                    Column() {
                                        Text(text = title)
                                    }
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                ),
                                navigationIcon = {
                                    if (navController.previousBackStackEntry != null) {
                                        IconButton(onClick = {
                                            navController.popBackStack()
                                        }) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = "Back"
                                            )
                                        }
                                    }
                                },
                            )
                        }
                    },
                    bottomBar = {
                        if (!isLoginScreen) {
                            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                                Destination.entries.forEachIndexed { index, destination ->
                                    NavigationBarItem(
                                        selected = selectedDestination == index,
                                        onClick = {
                                            navController.navigate(route = destination.route)
                                            selectedDestination = index
                                        },
                                        icon = {
                                            Icon(
                                                destination.icon,
                                                contentDescription = destination.title
                                            )
                                        },
                                        label = { Text(destination.label) }
                                    )
                                }
                            }
                        }
                    }) { innerPadding ->
                        NavHost(navController, startDestination = startDest) {
                            composable(route = Routes.POPULAR) {
                                PopularMovies(innerPadding, {
                                    titleId -> navController.navigate(Routes.navigateToMovieDetailsById(titleId))
                                })
                            }
                            composable(route = Routes.FAVOURITES) {
                                FavouritesMovies()
                            }
                            composable(route = Routes.SEARCH) {
                                SearchMovies()
                            }
                            composable(route = Routes.MOVIE_DETAILS) { backStackEntry ->
                                val titleId = backStackEntry.arguments?.getString(Routes.TITLE_ID) ?: "0"
                                MovieDetails(titleId)
                            }
                            composable(Routes.LOGIN) {
                                Login(innerPadding, navController)
                            }
                        }
                }
            }
        }
    }
}