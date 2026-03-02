package com.example.moviesandmore.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val title: String
) {
    POPULAR(Routes.POPULAR, "Popular", Icons.Default.Home, "Popular Movies"),
    FAVOURITES(Routes.FAVOURITES, "Favourites", Icons.Default.Star, "My Favourites"),
    SEARCH(Routes.SEARCH, "Search", Icons.Default.Search, "Search Movies"),
    ALBUM(Routes.ALBUM, "Photos", Icons.Default.PhotoLibrary, "Upload Photos"),
    RICK_AND_MORTY(Routes.RICK_AND_MORTY, "Characters", Icons.Default.Face, "Rick & Morty Characters")
}