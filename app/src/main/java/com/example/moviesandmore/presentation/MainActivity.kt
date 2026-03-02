package com.example.moviesandmore.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moviesandmore.ui.theme.MoviesAndMoreTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.composable
import com.example.moviesandmore.presentation.ui.components.AlbumScreen
import com.example.moviesandmore.presentation.ui.components.FavouritesMovies
import com.example.moviesandmore.presentation.ui.components.Login
import com.example.moviesandmore.presentation.ui.components.MovieDetails
import com.example.moviesandmore.presentation.ui.components.PopularMovies
import com.example.moviesandmore.presentation.ui.components.RickAndMortyScreen
import com.example.moviesandmore.presentation.ui.components.SearchMovies
import com.google.firebase.messaging.FirebaseMessaging

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val sharedPref = getSharedPreferences("movie_prefs", MODE_PRIVATE)
        val username = sharedPref.getString("username", null)
        val startDest = if (username == "dummy_username") Routes.POPULAR else Routes.LOGIN
        enableEdgeToEdge()
        NotificationChannel(
            "LOGIN_CHANNEL",
            "Login Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            Log.d("FCM_TOKEN", task.result ?: "Failed")
        }
        setContent {
            MoviesAndMoreTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val startDestination = Destination.POPULAR
                var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }
                val isLoginScreen = currentRoute == Routes.LOGIN
                val configuration = LocalConfiguration.current
                val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (!isLoginScreen && !isLandscape) {
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
                        if (!isLoginScreen && !isLandscape) {
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
                                FavouritesMovies(innerPadding, {
                                    titleId -> navController.navigate(Routes.navigateToMovieDetailsById(titleId))
                                })
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
                            composable(route = Routes.ALBUM) {
                                AlbumScreen(
                                    innerPadding,
                                    viewModel = viewModel()
                                )
                            }
                            composable(route = Routes.RICK_AND_MORTY) {
                                RickAndMortyScreen(innerPadding)
                            }
                        }
                }
            }
        }
    }
}