package com.example.moviesandmore.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moviesandmore.presentation.CharacterIntent
import com.example.moviesandmore.presentation.CharacterViewModel

@Composable
fun RickAndMortyScreen(
    innerPadding: PaddingValues,
    viewModel: CharacterViewModel = hiltViewModel()
) {
    val state by viewModel.state

    LaunchedEffect(Unit) {
        viewModel.handleIntent(CharacterIntent.LoadCharacters)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (state.error != null) {
            Text(
                text = state.error ?: "Unknown error",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.characters) { character ->
                    CharacterItem(character)
                }
            }
        }
    }
}
